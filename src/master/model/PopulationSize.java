package master.model;

import beast.core.BEASTObject;
import beast.core.Description;
import beast.core.Input;
import master.model.parsers.MASTERGrammarBaseListener;
import master.model.parsers.MASTERGrammarLexer;
import master.model.parsers.MASTERGrammarParser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.*;

/**
 * Beast 2 object for specifying the size of a population.
 *
 * @author Tim Vaughan
 */
@Description("Size of a particular population.")
public class PopulationSize extends BEASTObject {

    public Input<Population> populationInput = new Input<>(
            "population", "Population whose size to specify.");

    public Input<Double> sizeInput = new Input<>(
            "size", "Size of chosen population.");

    public Input<String> valueInput = new Input<>(
            "value", "Population size assignment string.");

    public Input<List<Predicate>> predicatesInput = new Input<>(
            "predicate",
            "Predicate used to determine which population sizes to include.",
            new ArrayList<>());

    Map<Population, Double> popSizes = null;

    public PopulationSize() { }

    @Override
    public void initAndValidate() throws Exception {
        if (populationInput.get() != null) {
            if (sizeInput.get() == null)
                throw new IllegalArgumentException("PopulationSize: population " +
                        "input given with no corresponding size input.");
        }
    }

    /**
     * Recursive function used to assemble list of variable indices.  The reason
     * we have to use a function like this (rather than a bunch of nested for
     * loops) is that the number of variables whose values need to be iterated
     * over is model-dependent.
     *
     * @param depth recursion depth
     * @param maxIndices list of bounds on variable values
     * @param indices variable value array
     * @param indicesList list to be filled with variable value arrays
     */
    private void variableLoop(int depth, int[] maxIndices, int[] indices, List<int[]> indicesList) {
        if (depth==indices.length) {
            indicesList.add(Arrays.copyOf(indices, indices.length));
        }  else {
            for (int i=0; i<maxIndices[depth]; i++) {
                indices[depth] = i;
                variableLoop(depth+1, maxIndices, indices, indicesList);
            }
        }
    }

    private void parseAssignmentString(String assignmentString,
                                       List<PopulationType> populationTypes,
                                       Map<String, Function> functionMap) {
        ANTLRInputStream inputStream = new ANTLRInputStream(assignmentString);

        // Custom parse/lexer error listener
        BaseErrorListener errorListener = new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer,
                                    Object offendingSymbol,
                                    int line, int charPositionInLine,
                                    String msg, RecognitionException e) {
                throw new RuntimeException("Error parsing character " +
                        charPositionInLine + " of line " + line +
                        " of population size assignment: " + msg);
            }
        };

        MASTERGrammarLexer lexer = new MASTERGrammarLexer(inputStream);
        lexer.removeErrorListeners();
        lexer.addErrorListener(errorListener);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        MASTERGrammarParser parser = new MASTERGrammarParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);

        ParseTree assignmentParseTree = parser.assignment();

        // Grab lists of population types and variable names, keeping track
        // of maximum values location index variables can take.
        Map<String, PopulationType> popTypes = new HashMap<>();
        Map<String, Integer> varNameBoundsMap = new HashMap<>();
        new ParseTreeWalker().walk(new MASTERGrammarBaseListener() {

            @Override
            public void exitPopel(MASTERGrammarParser.PopelContext ctx) {

                PopulationType popType = null;
                for (PopulationType thisPopType : populationTypes) {
                    if (ctx.popname().IDENT().toString().equals(thisPopType.getName()))
                        popType = thisPopType;
                }

                if (popType == null) {
                    System.err.println("Uknown population type '"
                        + ctx.popname().IDENT() + "' in reaction string.");
                    System.exit(1);
                }

                popTypes.put(ctx.popname().IDENT().toString(), popType);

                if (ctx.loc() != null) {

                    if (ctx.loc().locel().size() != popType.dims.length) {
                        System.err.println("Population location vector"
                            + " length does not match length of dims vector.");
                        System.exit(0);
                    }

                    for (int i=0; i<ctx.loc().locel().size(); i++) {
                        if (ctx.loc().locel(i).IDENT() != null) {
                            String varName = ctx.loc().locel(i).IDENT().toString();
                            if (!varNameBoundsMap.containsKey(varName)) {
                                varNameBoundsMap.put(varName, popType.dims[i]);
                            } else {
                                if (varNameBoundsMap.get(varName)>popType.dims[i]) {
                                    varNameBoundsMap.put(varName, popType.dims[i]);
                                }
                            }
                        }
                    }
                }
            }

        }, assignmentParseTree);

        // Assemble list of variable names and array of variable bounds
        List<String> scalarVarNames = new ArrayList<>(varNameBoundsMap.keySet());
        int[] scalarVarBounds = new int[scalarVarNames.size()];
        for (int i=0; i<scalarVarNames.size(); i++)
            scalarVarBounds[i] = varNameBoundsMap.get(scalarVarNames.get(i));

        // Use bounds array to assemble list of variable value arrays
        List<int[]> variableValuesList = new ArrayList<>();
        variableLoop(0, scalarVarBounds, new int[scalarVarNames.size()], variableValuesList);

        // Add population type dimensions as additional variables:
        List<String> vectorVarNames = new ArrayList<>();
        List<Double[]> vectorVarVals = new ArrayList<>();
        for (int i=0; i<populationTypes.size(); i++) {
            PopulationType popType = populationTypes.get(i);
            vectorVarNames.add(popType.getName() + "_dim");
            vectorVarVals.add(new Double[popType.getDims().length]);
            for (int j=0; j<popType.getDims().length; j++) {
                vectorVarVals.get(i)[j] = new Double(popType.getDims()[j]);
            }
        }


        // Consider every combination of variable values
        for (int[] scalarVarVals : variableValuesList) {

            // Test predicates to find whether this combination is allowed
            boolean include = true;
            for (Predicate pred : predicatesInput.get()) {
                if (!pred.isTrue(scalarVarNames, scalarVarVals,
                        vectorVarNames, vectorVarVals, functionMap)) {
                    include = false;
                    break;
                }
            }

            // Skip this population
            if (!include)
                continue;

            // TODO Create population object
        }
    }



    /**
     * Obtain the population size map implied by the inputs to this
     * PopulationSize object.
     *
     * @return map from populations to their sizes.
     */
    public Map<Population, Double> getPopSizes(Model model) {

        if (popSizes != null)
            return popSizes;

        popSizes = new HashMap<>();

        if (populationInput.get() != null)
            popSizes.put(populationInput.get(), sizeInput.get());

        if (valueInput.get() != null) {

        }

        return popSizes;
    }
}
