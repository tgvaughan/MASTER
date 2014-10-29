package master;

import beast.core.Input;
import beast.core.StateNode;
import beast.core.StateNodeInitialiser;
import beast.evolution.alignment.Alignment;
import beast.evolution.alignment.TaxonSet;
import beast.evolution.tree.*;

import java.util.List;

/**
 * User: Denise
 * Date: 06.06.14
 * Time: 16:34
 */
public class InitialTreeFromMaster extends Tree implements StateNodeInitialiser {

    public Input<BeastTreeFromMaster> masterTreeInput = new Input<BeastTreeFromMaster>(
            "masterTree",
            "The tree from which traits should be inherited", Input.Validate.REQUIRED);

    public Input<StateNode> treeInput = new Input<StateNode>(
            "tree",
            "The initial tree which is to inherited traits from masterTree", Input.Validate.REQUIRED);

    public Input<Alignment> taxaInput = new Input<Alignment>("taxa", "set of taxa to initialise tree specified by alignment");


    StateNode tree;

    @Override
    public void initAndValidate() throws Exception {

        super.initAndValidate();

        initStateNodes();
    }

    public void initStateNodes() throws Exception{

        BeastTreeFromMaster masterTree = masterTreeInput.get();


        tree  = new RandomTree();

        TraitSet typeTrait = new TraitSet();
        TraitSet dateTrait = new TraitSet();

        Alignment taxa = taxaInput.get();
        TaxonSet taxonset = new TaxonSet();
        taxonset.initByName("alignment", taxa);

        String types = "";
        String dates = "";

        for (Node beastNode : masterTree.getExternalNodes()){

            dates += beastNode.getNr() + "=" + beastNode.getHeight() +",";
            types += beastNode.getNr() + "=" + ((int[])beastNode.getMetaData("location"))[0] +",";

        }

        dates = dates.substring(0,dates.length()-1);
        types = types.substring(0,types.length()-1);

        typeTrait.initByName("value", types, "taxa", taxonset, "traitname", "type");
        dateTrait.initByName("value", dates, "taxa", taxonset, "traitname", "date-backward");

        tree.initByName("trait",dateTrait,"trait",typeTrait, "taxa", taxa, "populationModel", ((RandomTree)treeInput.get()).populationFunctionInput.get());

        treeInput.get().setInputValue("trait",dateTrait);
        treeInput.get().setInputValue("trait",typeTrait);
        treeInput.get().assignFromWithoutID(tree);
    }

    public void getInitialisedStateNodes(List<StateNode> stateNodes){

        stateNodes.add(treeInput.get());
    }

}


