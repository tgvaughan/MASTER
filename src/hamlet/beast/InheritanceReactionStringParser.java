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
package hamlet.beast;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class InheritanceReactionStringParser {
    
    private List<hamlet.inheritance.Node> reactants, products;
    private List<Integer> reactantIDs, productIDs;
    
    private String string;
    private List<hamlet.PopulationType> popTypes;
    private Map<String, hamlet.PopulationType> popTypeMap;
        
    private enum Token {
        SPACE, INT, LABEL, STARTLOC, ENDLOC, COMMA, COLON, PLUS, ARROW, END;
    }
    
    private List<Token> tokenList;
    private List<String> valueList;
    
    private int parseIdx;
    
    private Map<hamlet.PopulationType, Integer> nextNodeID;
    
    public InheritanceReactionStringParser(String string, List<hamlet.PopulationType> popTypes) throws ParseException {
       
        this.string = string.trim();        
        this.popTypes = popTypes;
        
        // Construct map from pop type names to pop type objects.
        popTypeMap = Maps.newHashMap();
        for (hamlet.PopulationType popType : popTypes)
            popTypeMap.put(popType.getName(), popType);
        
        doLex();
        doRecursiveDecent();
        
        // Create inheritance relationships:
        for (int r=0; r<reactants.size(); r++) {
            for (int p=0; p<products.size(); p++) {
                if (productIDs.get(p)==reactantIDs.get(r)) {
                    reactants.get(r).addChild(products.get(p));
                }
            }
        }
    }    
    
    public hamlet.inheritance.Node[] getReactants() {
        return reactants.toArray(new hamlet.inheritance.Node[0]);
    }
    
    public hamlet.inheritance.Node[] getProducts() {
        return products.toArray(new hamlet.inheritance.Node[0]);
    }
    
    private void doLex() throws ParseException {
        
        Map<Token, Pattern> tokenPatterns = Maps.newHashMap();
               
        tokenPatterns.put(Token.SPACE, Pattern.compile("\\s+"));
        tokenPatterns.put(Token.INT, Pattern.compile("\\d+"));
        tokenPatterns.put(Token.LABEL, Pattern.compile("[a-zA-Z_]\\w*"));
        tokenPatterns.put(Token.STARTLOC, Pattern.compile("\\["));
        tokenPatterns.put(Token.ENDLOC, Pattern.compile("\\]"));
        tokenPatterns.put(Token.COLON, Pattern.compile(":"));
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
        reactantIDs = Lists.newArrayList();
        products = Lists.newArrayList();
        productIDs = Lists.newArrayList();
        nextNodeID = Maps.newHashMap();
        
        for (hamlet.PopulationType popType : popTypes)
            nextNodeID.put(popType, 0);
                
        ruleS(reactants, reactantIDs);
        acceptToken(Token.ARROW, true);
        
        for (hamlet.PopulationType popType : popTypes)
            nextNodeID.put(popType, 0);
        
        ruleS(products, productIDs);
        acceptToken(Token.END, true);
    }
    
    /**
     * S -> zero | PQ
     * 
     * @param poplist
     * @param idlist
     * @throws ParseException 
     */
    private void ruleS(List<hamlet.inheritance.Node> nodelist, List<Integer> idlist) throws ParseException {
        
        // Deal special case of "0":
        if (acceptToken(Token.INT, false)) {
            if (valueList.get(parseIdx-1).equals("0"))
                return;
            else {
                //backtrack
                parseIdx -= 1;
            }
        }

        ruleP(nodelist, idlist);
        ruleQ(nodelist, idlist);
    }    
    
    /**
     * Q -> plus PQ | eps
     * 
     * @param nodelist
     * @param idlist 
     * @throws ParseException 
     */
    private void ruleQ(List<hamlet.inheritance.Node> nodelist, List<Integer> idlist) throws ParseException {
        if (acceptToken(Token.PLUS, false)) {
            ruleP(nodelist, idlist);
            ruleQ(nodelist, idlist);
        }
        // else accept epsilon
    }
    
    /**
     * P -> FLDI
     * @param nodelist
     * @param idlist 
     * @throws ParseException 
     */
    private void ruleP(List<hamlet.inheritance.Node> nodelist, List<Integer> idlist) throws ParseException {
        int factor = ruleF();
        String popName = ruleL();
        int[] loc = ruleD();
        int id = ruleI();
        
        hamlet.Population pop = new hamlet.Population(popTypeMap.get(popName), loc);
        for (int i=0; i<factor; i++)
            nodelist.add(new hamlet.inheritance.Node(pop));
        
        if (id<0) {
            id = nextNodeID.get(pop.getType());
            for (int i=0; i<factor; i++) {
                idlist.add(id);
                id += 1;
            }
        } else {
            for (int i=0; i<factor; i++)
                idlist.add(id);
        }
        nextNodeID.put(pop.getType(), id);
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
        acceptToken(Token.LABEL, true);
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
     * I -> : int | eps
     * 
     * @return
     * @throws ParseException 
     */
    private int ruleI() throws ParseException {
        int id;
        if (acceptToken(Token.COLON, false)) {
            acceptToken(Token.INT, true);
            id = Integer.parseInt(valueList.get(parseIdx-1));
        } else
            id = -1;
        
        return id;
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
