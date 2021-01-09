package fr.catcore.gamegui.accessor;

public interface AnvilScreenHandlerAccessor {

    int getLevelCost_server();

    void setLevelCost_server(int cost);

    int getRepairItemUsage();

    void setRepairItemUsage(int repairItemUsage);

    String getNewItemName();
}
