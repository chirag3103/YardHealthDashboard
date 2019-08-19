package com.navis.dashboard;

import java.util.ArrayList;
import java.util.List;

/*
 Data structure for yard information.
 */
public interface YardInterface {

    // Returns List of block IDs in the yard.
    ArrayList<String> getBlocks();

    // Get all stacks in yard.
    ArrayList<ArrayList<Container>> getStacks();

    // Get all the stacks in a block.
    ArrayList<ArrayList<Container>> getStacks(String block);

    // Returns all the stacks in a ROW.
    ArrayList<ArrayList<Container>> getRow(String row);

    // Returns all the stacks in a COL.
    ArrayList<ArrayList<Container>> getColumn(String col);

    // Add container to stack in yard by UNITNBR, UNITPOSITION and ESTMVTIME. If the stack does not exists, create it.
    void addContainer(String unitNbr, String unitPosition, long estMvTime, String kind);
}
