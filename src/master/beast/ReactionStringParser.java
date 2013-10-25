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
 * A parser for strings specifying reactions and inheritance reactions.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class ReactionStringParser {
    
    // Outputs of the parser.  The `node ID' lists are used to
    // assign inheritance relationships.
    public List<Integer> reactantIDs, productIDs;
    public List<String> reactantPopNames, productPopNames;
    public List<List<Integer>> reactantLocs, productLocs;
    public List<String> variableNames;
    
    private String string;
    private Map<String, master.PopulationType> popTypeMap;
    
    // Available tokens:
    private enum Token {
        SPACE, INT, LABEL, STARTLOC, ENDLOC, COMMA, COLON, PLUS, ARROW, END;
    }
    
    // Lists of tokens and values appearing in string:
    private List<Token> tokenList;
    private List<String> valueList;
    
    // This parser abuses fields by treating them as global variables:
    private int parseIdx;    
    private int nextNodeID;
    private Map<master.PopulationType,Integer> seenTypeIDs;
    
    /**
     * Assemble lists of reactant and product nodes by parsing a
     * (hopefully) human-readable string.
     * 
     * @param string String to parse
     * @param popTypes List of population types. Needed to interpret population
     * labels occurring in string.
     * @param ranges List of ranges
     * 
     * @throws ParseException Tries to be a tiny bit informative when things go wrong.
     */
    public ReactionStringParser(String string,
            List<master.PopulationType> popTypes) throws ParseException {
       
        this.string = string.trim();        
        
        // Construct map from pop type names to pop type objects.
        popTypeMap = Maps.newHashMap();
        for (master.PopulationType popType : popTypes)
            popTypeMap.put(popType.getName(), popType);
        
        doLex();
        doRecursiveDecent();

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
        
        reactantIDs = Lists.newArrayList();
        productIDs = Lists.newArrayList();
        reactantPopNames = Lists.newArrayList();
        productPopNames = Lists.newArrayList();
        reactantLocs = Lists.newArrayList();
        productLocs = Lists.newArrayList();
        variableNames = Lists.newArrayList();
        
        nextNodeID = 0;
        seenTypeIDs = Maps.newHashMap();
        
        ruleS(true);
        acceptToken(Token.ARROW, true);
        
        ruleS(false);
        acceptToken(Token.END, true);
    }
    
    /**
     * S -> zero | PQ
     * 
     * @param processingReactants 
     * @throws ParseException 
     */
    private void ruleS(boolean processingReactants) throws ParseException {
        
        // Deal special case of "0"
        if (acceptToken(Token.INT, false)) {
            if (valueList.get(parseIdx-1).equals("0"))
                return;
            else {
                //backtrack
                parseIdx -= 1;
            }
        }

        ruleP(processingReactants);
        ruleQ(processingReactants);
    }    
    
    /**
     * Q -> plus PQ | eps
     * 
     * @param processingReactants 
     * @throws ParseException 
     */
    private void ruleQ(boolean processingReactants) throws ParseException {
        if (acceptToken(Token.PLUS, false)) {
            ruleP(processingReactants);
            ruleQ(processingReactants);
        }
        // else accept epsilon
    }
    
    /**
     * P -> FLDI
     * 
     * @param processingReactants 
     * @throws ParseException 
     */
    private void ruleP(boolean processingReactants) throws ParseException {
        int factor = ruleF();
        String popName = ruleL();
        List<Integer>loc = ruleD();
        int chosenid = ruleI();
        
        // Check that population specifier name exists:
        if (!popTypeMap.containsKey(popName))
            throw new ParseException("Unknown population type name '"
                    + popName + "' encountered in reaction string.", parseIdx);
        
        master.PopulationType popType = popTypeMap.get(popName);
        
        // Ensure location is valid if given.
//        if (loc.length>0 && !popTypeMap.get(popName).containsLocation(loc))
//            throw new ParseException("Population type '" + popName
//                    + "' does not contain specified location.", parseIdx);
        
        for (int i=0; i<factor; i++) {
            if (processingReactants) {
                reactantPopNames.add(popName);
                reactantLocs.add(loc);
            } else {
                productPopNames.add(popName);
                productLocs.add(loc);
            }
        }
        
        // Automatic assignment of inheritance relationships.
        // This default behaviour is to make the first reactant node of a given
        // type the parent of all product nodes of the same type.
        int id;
        
        for (int i=0; i<factor; i++) {
            if (chosenid<0) {
                if (processingReactants) {
                    id = nextNodeID++;
                    if (!seenTypeIDs.containsKey(popType))
                        seenTypeIDs.put(popType, id);
                } else {
                    if (seenTypeIDs.containsKey(popType))
                        id = seenTypeIDs.get(popType);
                    else
                        id = nextNodeID++;
                }
            } else {
                id = chosenid;
            }
        
            if (processingReactants)
                reactantIDs.add(id);
            else
                productIDs.add(id);
        }
        
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
     * D -> [ int | label | M ] | eps
     * 
     * @return
     * @throws ParseException 
     */
    private List<Integer> ruleD() throws ParseException {
        List<Integer> locList = Lists.newArrayList();
        if (acceptToken(Token.STARTLOC, false)) {
            if (acceptToken(Token.INT, false)) {
                locList.add(Integer.parseInt(valueList.get(parseIdx-1)));
            } else {
                acceptToken(Token.LABEL, true);
                if (!variableNames.contains(valueList.get(parseIdx-1)))
                    variableNames.add(valueList.get(parseIdx-1));
                locList.add(-(1+variableNames.indexOf(valueList.get(parseIdx-1))));
            }

            ruleM(locList);
            acceptToken(Token.ENDLOC, true);
        }
        
        return locList;
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
            if (acceptToken(Token.INT, false)) {
                locList.add(Integer.parseInt(valueList.get(parseIdx-1)));
            } else {
                acceptToken(Token.LABEL, true);
                if (!variableNames.contains(valueList.get(parseIdx-1)))
                    variableNames.add(valueList.get(parseIdx-1));
                locList.add(-(1+variableNames.indexOf(valueList.get(parseIdx-1))));
            }
            ruleM(locList);
        }
        // else accept epsilon
    }
}
