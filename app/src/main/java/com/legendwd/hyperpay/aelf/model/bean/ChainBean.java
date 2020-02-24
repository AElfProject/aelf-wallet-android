package com.legendwd.hyperpay.aelf.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChainBean {


    /**
     * ChainId : AELF
     * Branches : {"BVzZde+wYKbWj+zvd9ixtHFzan4mG72vEJyjE1sPuPU=":625305}
     * NotLinkedBlocks : []
     * LongestChainHeight : 625305
     * LongestChainHash : 055cd975efb060a6d68fecef77d8b1b471736a7e261bbdaf109ca3135b0fb8f5
     * GenesisBlockHash : b79e3b3c79190835d35fad03edffe62f3f6571427832a88e00d00db3851d18af
     * GenesisContractAddress : 2gaQh4uxg6tzyH1ADLoDxvHA14FMpzEiMqsQ6sDG5iHT8cmjp8
     * LastIrreversibleBlockHash : fc7c5013e47fc33647dd2b4e15b88f4fad4d58b4b763626d0666fbdfc6cb3294
     * LastIrreversibleBlockHeight : 625290
     * BestChainHash : 055cd975efb060a6d68fecef77d8b1b471736a7e261bbdaf109ca3135b0fb8f5
     * BestChainHeight : 625305
     */

    private String ChainId;
    private BranchesBean Branches;
    private int LongestChainHeight;
    private String LongestChainHash;
    private String GenesisBlockHash;
    private String GenesisContractAddress;
    private String LastIrreversibleBlockHash;
    private int LastIrreversibleBlockHeight;
    private String BestChainHash;
    private int BestChainHeight;
    private List<?> NotLinkedBlocks;

    public String getChainId() {
        return ChainId;
    }

    public void setChainId(String ChainId) {
        this.ChainId = ChainId;
    }

    public BranchesBean getBranches() {
        return Branches;
    }

    public void setBranches(BranchesBean Branches) {
        this.Branches = Branches;
    }

    public int getLongestChainHeight() {
        return LongestChainHeight;
    }

    public void setLongestChainHeight(int LongestChainHeight) {
        this.LongestChainHeight = LongestChainHeight;
    }

    public String getLongestChainHash() {
        return LongestChainHash;
    }

    public void setLongestChainHash(String LongestChainHash) {
        this.LongestChainHash = LongestChainHash;
    }

    public String getGenesisBlockHash() {
        return GenesisBlockHash;
    }

    public void setGenesisBlockHash(String GenesisBlockHash) {
        this.GenesisBlockHash = GenesisBlockHash;
    }

    public String getGenesisContractAddress() {
        return GenesisContractAddress;
    }

    public void setGenesisContractAddress(String GenesisContractAddress) {
        this.GenesisContractAddress = GenesisContractAddress;
    }

    public String getLastIrreversibleBlockHash() {
        return LastIrreversibleBlockHash;
    }

    public void setLastIrreversibleBlockHash(String LastIrreversibleBlockHash) {
        this.LastIrreversibleBlockHash = LastIrreversibleBlockHash;
    }

    public int getLastIrreversibleBlockHeight() {
        return LastIrreversibleBlockHeight;
    }

    public void setLastIrreversibleBlockHeight(int LastIrreversibleBlockHeight) {
        this.LastIrreversibleBlockHeight = LastIrreversibleBlockHeight;
    }

    public String getBestChainHash() {
        return BestChainHash;
    }

    public void setBestChainHash(String BestChainHash) {
        this.BestChainHash = BestChainHash;
    }

    public int getBestChainHeight() {
        return BestChainHeight;
    }

    public void setBestChainHeight(int BestChainHeight) {
        this.BestChainHeight = BestChainHeight;
    }

    public List<?> getNotLinkedBlocks() {
        return NotLinkedBlocks;
    }

    public void setNotLinkedBlocks(List<?> NotLinkedBlocks) {
        this.NotLinkedBlocks = NotLinkedBlocks;
    }

    public static class BranchesBean {
        @SerializedName("BVzZde+wYKbWj+zvd9ixtHFzan4mG72vEJyjE1sPuPU=")
        private int _$BVzZdeWYKbWjZvd9ixtHFzan4mG72vEJyjE1sPuPU227; // FIXME check this code

        public int get_$BVzZdeWYKbWjZvd9ixtHFzan4mG72vEJyjE1sPuPU227() {
            return _$BVzZdeWYKbWjZvd9ixtHFzan4mG72vEJyjE1sPuPU227;
        }

        public void set_$BVzZdeWYKbWjZvd9ixtHFzan4mG72vEJyjE1sPuPU227(int _$BVzZdeWYKbWjZvd9ixtHFzan4mG72vEJyjE1sPuPU227) {
            this._$BVzZdeWYKbWjZvd9ixtHFzan4mG72vEJyjE1sPuPU227 = _$BVzZdeWYKbWjZvd9ixtHFzan4mG72vEJyjE1sPuPU227;
        }
    }
}
