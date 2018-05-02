package network.minter.mintercore.models;

import network.minter.mintercore.models.operational.OperationType;

/**
 * MinterCore. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class HistoryTransaction {
    public String hash;
    public OperationType type;
    public String from;
    public String to;
    public String coinSymbol;
    public float value;


}
