package master.model;

import beast.base.core.BEASTObject;
import beast.base.core.Description;
import beast.base.core.Input;
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
    public void initAndValidate() {
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

    /**
     * Do all the work of parsing the population size assignment string.
     *
     * @param assignmentString string to parse
     * @param populationTypes list of valid population types
     * @param functionMap map from function names to objects
     */
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
        ParseTreeWalker walker = new ParseTreeWalker();

        // Grab lists of population types and variable names, keeping track
        // of maximum values location index variables can take.
        PreprocessingListener listener = new PreprocessingListener(populationTypes);
        walker.walk(listener, assignmentParseTree);

        // Assemble list of variable names and array of variable bounds
        List<String> scalarVarNames = new ArrayList<>(listener.getVarNameBoundsMap().keySet());
        int[] scalarVarBounds = new int[scalarVarNames.size()];
        for (int i=0; i<scalarVarNames.size(); i++)
            scalarVarBounds[i] = listener.getVarNameBoundsMap().get(scalarVarNames.get(i));

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
                vectorVarVals.get(i)[j] = (double) popType.getDims()[j];
            }
        }

        ExpressionEvaluator evaluator = new ExpressionEvaluator(
                listener.getExpressionParseTree(), scalarVarNames, functionMap);

        PopulationType popType = listener.getSeenPopulationTypes().get(0);


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

            // Evaluate expression to find size
            Double[] sizeExprValue = evaluator.evaluate(scalarVarVals);
            if (sizeExprValue.length != 1)
                throw new IllegalArgumentException("Population size expression " +
                        "must evaluate to scalar.");
            double popSize = sizeExprValue[0];

            // Create population object
            walker.walk(new MASTERGrammarBaseListener() {

                @Override
                public void exitAssignment(@NotNull MASTERGrammarParser.AssignmentContext ctx) {
                    // Assemble loc

                    List<Integer> locList = new ArrayList<>();
                    if (ctx.loc() != null) {
                        for (MASTERGrammarParser.LocelContext locelCtx : ctx.loc().locel()) {
                            if (locelCtx.IDENT() == null) {
                                locList.add(Integer.parseInt(locelCtx.getText()));
                            } else {
                                String varName = locelCtx.IDENT().getText();
                                int varIdx = scalarVarNames.indexOf(varName);
                                locList.add(scalarVarVals[varIdx]);
                            }
                        }
                    }

                    int[] loc = new int[locList.size()];
                    for (int i=0; i<loc.length; i++)
                        loc[i] = locList.get(i);

                    popSizes.put(new Population(popType, loc), popSize);
                }
            }, assignmentParseTree);
        }
    }


    /**
     * Compute the population sizes implied by the inputs to this
     * PopulationSize object. Must be called before getPopSizes().
     *
     * @param model corresponding MASTER model
     */
    public void computePopulationSizes(Model model) {
        popSizes = new HashMap<>();

        if (populationInput.get() != null)
            popSizes.put(populationInput.get(), sizeInput.get());

        if (valueInput.get() != null) {
            parseAssignmentString(valueInput.get(),
                    model.getPopulationTypes(), model.getFunctionMap());
        }
    }

    /**
     * Obtain the population size map implied by the inputs to this
     * PopulationSize object.
     *
     * @return map from populations to their sizes.
     */
    public Map<Population, Double> getPopSizes() {
        return popSizes;
    }


    /**
     * Listener for extracting population type, variable names
     * and bounds, and expression subtree from assignment parse tree.
     */
    private class PreprocessingListener extends MASTERGrammarBaseListener {

        private ParseTree expressionParseTree;
        private Map<String, PopulationType> seenPopulationTypeMap = new HashMap<>();
        private Map<String, Integer> varNameBoundsMap = new HashMap<>();

        List<PopulationType> populationTypes;

        /**
         * Construct a MASTER grammar listener that assembles the list of
         * population types and location variables used by a reaction string
         * or population size init string parse tree.
         *
         * @param populationTypes List of available population types.
         */
        public PreprocessingListener(List<PopulationType> populationTypes) {
            this.populationTypes = populationTypes;
        }

        /**
         * Retrieve population types found in the parse tree.
         *
         * @return map from population type names to the corresponding objects
         */
        public Map<String, PopulationType> getSeenPopulationTypeMap() {
            return seenPopulationTypeMap;
        }

        public List<PopulationType> getSeenPopulationTypes() {
            return new ArrayList<>(seenPopulationTypeMap.values());
        }

        /**
         * Retrieve location variable bounds map.
         *
         * @return map from variable names to maximum allowed value
         */
        public Map<String, Integer> getVarNameBoundsMap() {
            return varNameBoundsMap;
        }

        /**
         * @return parse tree for assignment expression.
         */
        public ParseTree getExpressionParseTree() {
            return expressionParseTree;
        }

        @Override
        public void exitAssignment(@NotNull MASTERGrammarParser.AssignmentContext ctx) {


            PopulationType popType = null;
            for (PopulationType thisPopType : populationTypes) {
                if (ctx.popname().IDENT().toString().equals(thisPopType.getName()))
                    popType = thisPopType;
            }

            if (popType == null) {
                System.err.println("Unknown population type '"
                        + ctx.popname().IDENT() + "' in reaction string.");
                System.exit(1);
            }

            seenPopulationTypeMap.put(ctx.popname().getText(), popType);

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

            expressionParseTree = ctx.expression();
        }
    }
}
