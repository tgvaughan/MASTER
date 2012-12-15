/*
 * Copyright (C) 2012 Tim Vaughan <tgvaughan@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package master.beast;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A parser for strings specifying individual reactions.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class ReactionStringParser {
    
    private List<master.Population> reactants;
    private List<master.Population> products;
    
    private String string;
    private Map<String, master.PopulationType> popTypeMap;
        
    private enum Token {
        SPACE, INT, POPLABEL, STARTLOC, ENDLOC, COMMA, PLUS, ARROW, END;
    }
    
    private List<Token> tokenList;
    private List<String> valueList;
    
    private int parseIdx;
    
    public ReactionStringParser(String string, List<master.PopulationType> popTypes) throws ParseException {
       
        this.string = string.trim();
        
        // Construct map from pop type names to pop type objects.
        popTypeMap = Maps.newHashMap();
        for (master.PopulationType popType : popTypes)
            popTypeMap.put(popType.getName(), popType);
        
        doLex();
        doRecursiveDecent();
    }    
    
    public master.Population[] getReactants() {
        return reactants.toArray(new master.Population[0]);
    }
    
    public master.Population[] getProducts() {
        return products.toArray(new master.Population[0]);
    }
    
    private void doLex() throws ParseException {
        
        Map<Token, Pattern> tokenPatterns = Maps.newHashMap();
               
        tokenPatterns.put(Token.SPACE, Pattern.compile("\\s+"));
        tokenPatterns.put(Token.INT, Pattern.compile("\\d+"));
        tokenPatterns.put(Token.POPLABEL, Pattern.compile("[a-zA-Z_]\\w*"));
        tokenPatterns.put(Token.STARTLOC, Pattern.compile("\\["));
        tokenPatterns.put(Token.ENDLOC, Pattern.compile("\\]"));
        tokenPatterns.put(Token.COMMA, Pattern.compile(","));        
        tokenPatterns.put(Token.PLUS, Pattern.compile("\\+"));
        tokenPatterns.put(Token.ARROW, Pattern.compile("->"));
                
        tokenList = Lists.newArrayList();
        valueList = Lists.newArrayList();
                
        // Parse index
        parseIdx=0;

        while (parseIdx<string.length()) {
            
            boolean matched = false;
            for (Token token : tokenPatterns.keySet()) {
                Matcher matcher = tokenPatterns.get(token).matcher(string.substring(parseIdx));
                if (matcher.find() && matcher.start()==0) {
                    parseIdx += matcher.group().length();
                    matched = true;
                    
                    // Discard whitespace:
                    if (token == Token.SPACE)
                        break;
                    
                    tokenList.add(token);
                    valueList.add(matcher.group());
                    
                    //System.out.println(token + ": " + matcher.group());  
                    break;
                }
            }
            
            if (!matched) {
                throw new ParseException("Error reading reaction string:"
                        + " couldn't match '" + string.substring(parseIdx) + "'", parseIdx);
            }
        }
        
        // Add dummy end token to specify end of input:
        tokenList.add(Token.END);
        valueList.add("");
    }
    
    private boolean acceptToken(Token token, boolean manditory) throws ParseException {
        if (tokenList.get(parseIdx).equals(token)) {
            parseIdx += 1;
            return true;
        } else {
            if (manditory)
                throw new ParseException(
                        "Error parsing token " + valueList.get(parseIdx)
                        + " (expected " + token + ")", parseIdx);
        }
            return false;
    }
    
    
    private void doRecursiveDecent() throws ParseException {
        parseIdx = 0;
        
        reactants = Lists.newArrayList();
        products = Lists.newArrayList();
                
        ruleS(reactants);
        acceptToken(Token.ARROW, true);
        ruleS(products);
        acceptToken(Token.END, true);
    }
    
    /**
     * S -> zero | PQ
     * 
     * @param poplist
     * @throws ParseException 
     */
    private void ruleS(List<master.Population> poplist) throws ParseException {
        
        // Deal special case of "0":
        if (acceptToken(Token.INT, false)) {
            if (valueList.get(parseIdx-1).equals("0"))
                return;
            else {
                //backtrack
                parseIdx -= 1;
            }
        }

        ruleP(poplist);
        ruleQ(poplist);
    }    
    
    /**
     * Q -> plus PQ | eps
     * 
     * @param poplist
     * @throws ParseException 
     */
    private void ruleQ(List<master.Population> poplist) throws ParseException {
        if (acceptToken(Token.PLUS, false)) {
            ruleP(poplist);
            ruleQ(poplist);
        }
        // else accept epsilon
    }
    
    /**
     * P -> FLD
     * @param poplist
     * @throws ParseException 
     */
    private void ruleP(List<master.Population> poplist) throws ParseException {
        int factor = ruleF();
        String popName = ruleL();
        int[] loc = ruleD();
        
        // Check that population specifier name exists:
        if (!popTypeMap.containsKey(popName))
            throw new ParseException("Unknown population type name '"
                    + popName + "' encountered in reaction string.", parseIdx);
        
        // Ensure location is valid if given.
        if (loc.length>0 && !popTypeMap.get(popName).containsLocation(loc))
            throw new ParseException("Population type '" + popName
                    + "' does not contain specified location.", parseIdx);
        
        
        for (int i=0; i<factor; i++)
            poplist.add(new master.Population(popTypeMap.get(popName), loc));
    }
    
    /**
     * F -> int | eps
     * 
     * @return
     * @throws ParseException 
     */
    private int ruleF() throws ParseException {
        if (acceptToken(Token.INT, false))
            return Integer.parseInt(valueList.get(parseIdx-1));
        else
            return 1;
    }
    
    /**
     * L -> label | eps
     * @return
     * @throws ParseException 
     */
    private String ruleL() throws ParseException {
        acceptToken(Token.POPLABEL, true);
        return valueList.get(parseIdx-1);
    }
    
    /**
     * D -> [ int M ] | eps
     * 
     * @return
     * @throws ParseException 
     */
    private int[] ruleD() throws ParseException {
        List<Integer> locList = Lists.newArrayList();
        if (acceptToken(Token.STARTLOC, false)) {
            acceptToken(Token.INT, true);
            locList.add(Integer.parseInt(valueList.get(parseIdx-1)));
            ruleM(locList);
            acceptToken(Token.ENDLOC, true);
        }
        
        int [] loc = new int[locList.size()];
        for (int i=0; i<loc.length; i++)
            loc[i] = locList.get(i);
        
        return loc;
    }
    
    /**
     * M -> comma M | eps
     * 
     * @param loc
     * @throws ParseException 
     */
    private void ruleM(List<Integer> locList) throws ParseException {
        if (acceptToken(Token.COMMA, false)) {
            acceptToken(Token.INT, true);
            locList.add(Integer.parseInt(valueList.get(parseIdx-1)));
            ruleM(locList);
        }
        // else accept epsilon
    }
}
