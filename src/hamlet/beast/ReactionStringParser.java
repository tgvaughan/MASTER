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
public class ReactionStringParser {
    
    private List<hamlet.Population> reactants;
    private List<hamlet.Population> products;
    
    private String string;
    private Map<String, hamlet.PopulationType> popTypeMap;
    
    private List<String> tokenList;
    private List<String> valueList;
    
    private int idx;
    
    public ReactionStringParser(String string, List<hamlet.PopulationType> popTypes) throws ParseException {
       
        this.string = string;
        
        // Construct map from pop type names to pop type objects.
        popTypeMap = Maps.newHashMap();
        for (hamlet.PopulationType popType : popTypes)
            popTypeMap.put(popType.getName(), popType);
        
        doLex();
        doRecursiveDecent();
    }    
    
    public hamlet.Population[] getReactants() {
        return (hamlet.Population[])reactants.toArray();
    }
    
    public hamlet.Population[] getProducts() {
        return (hamlet.Population[])products.toArray();
    }
        
    private void doLex() throws ParseException {
        
        List<String> tokens = Lists.newArrayList();
        List<Pattern> tokenPatterns = Lists.newArrayList();
               
        tokens.add("SPACE");
        tokenPatterns.add(Pattern.compile("\\s+"));
        
        tokens.add("ZERO");
        tokenPatterns.add(Pattern.compile("0"));
        
        tokens.add("INT");
        tokenPatterns.add(Pattern.compile("[1-9]\\d+"));
        
        tokens.add("POP");
        tokenPatterns.add(Pattern.compile("[a-zA-Z_]\\w*"));
        
        tokens.add("STARTLOC");
        tokenPatterns.add(Pattern.compile("\\["));
        
        tokens.add("COMMA");
        tokenPatterns.add(Pattern.compile(","));
        
        tokens.add("ENDLOC");
        tokenPatterns.add(Pattern.compile("\\]"));
        
        tokens.add("PLUS");
        tokenPatterns.add(Pattern.compile("\\+"));
        
        tokens.add("ARROW");
        tokenPatterns.add(Pattern.compile("->"));
                
        List <Population> reactantList = Lists.newArrayList();
        List <Population> productList = Lists.newArrayList();

        tokenList = Lists.newArrayList();
        valueList = Lists.newArrayList();
                
        // Parse index
        idx=0;

        while (idx<string.length()) {
            
            boolean matched = false;
            for (int tidx=0; tidx<tokens.size(); tidx++) {
                Matcher matcher = tokenPatterns.get(tidx).matcher(string.substring(idx));
                if (matcher.find() && matcher.start()==0) {
                    idx += matcher.group().length();
                    matched = true;
                    
                    // Discard whitespace:
                    if (tokens.get(tidx).equals("SPACE"))
                        break;
                    
                    tokenList.add(tokens.get(tidx));
                    valueList.add(matcher.group());
                    
                    System.out.println(tokens.get(tidx) + ": " + matcher.group());                    
                }
            }
            
            if (!matched) {
                throw new ParseException("Error reading reaction string.", idx);
            }
        }
    }
    
    private boolean acceptToken(String tokenStr, boolean manditory) throws ParseException {
        if (tokenList.get(idx).equals(tokenStr)) {
            idx += 1;
            return true;
        } else {
            if (manditory)
                throw new ParseException(
                        "Error parsing token " + valueList.get(idx)
                        + " (expected " + tokenStr + ")", idx);
        }
            return false;
    }
    
    
    private void doRecursiveDecent() throws ParseException {
        idx = 0;
        
        reactants = Lists.newArrayList();
        products = Lists.newArrayList();
                
        ruleS(reactants);
        acceptToken("ARROW", true);
        ruleS(products);
    }
    
    /**
     * S -> zero | PQ
     * 
     * @param poplist
     * @throws ParseException 
     */
    private void ruleS(List<hamlet.Population> poplist) throws ParseException {
        
        if (acceptToken("ZERO", false))
            return;

        ruleP(poplist);
        ruleQ(poplist);
    }    
    
    /**
     * Q -> plus PQ | eps
     * 
     * @param poplist
     * @throws ParseException 
     */
    private void ruleQ(List<hamlet.Population> poplist) throws ParseException {
        if (acceptToken("PLUS", false)) {
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
    private void ruleP(List<hamlet.Population> poplist) throws ParseException {
        int factor = ruleF();
        String popName = ruleL();
        int[] loc = ruleD();
        
        for (int i=0; i<factor; i++)
            poplist.add(new hamlet.Population(popTypeMap.get(popName), loc));
    }
    
    /**
     * F -> int | eps
     * 
     * @return
     * @throws ParseException 
     */
    private int ruleF() throws ParseException {
        if (acceptToken("INT", false))
            return Integer.parseInt(valueList.get(idx-1));
        else
            return 1;
    }
    
    /**
     * L -> label | eps
     * @return
     * @throws ParseException 
     */
    private String ruleL() throws ParseException {
        acceptToken("POP", true);
        return valueList.get(idx-1);
    }
    
    /**
     * D -> [ int M ] | eps
     * 
     * @return
     * @throws ParseException 
     */
    private int[] ruleD() throws ParseException {
        List<Integer> locList = Lists.newArrayList();
        if (acceptToken("STARTLOC", false)) {
            acceptToken("INT", true);
            locList.add(Integer.parseInt(valueList.get(idx-1)));
            ruleM(locList);
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
        if (acceptToken("COMMA", false)) {
            acceptToken("INT", true);
            locList.add(Integer.parseInt(valueList.get(idx-1)));
            ruleM(locList);
        }
        // else accept epsilon
    }
}
