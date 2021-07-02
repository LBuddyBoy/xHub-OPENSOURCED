package services.xenlan.hub.tablist.ziggurat.utils;


import services.xenlan.hub.tablist.ziggurat.ZigguratTablist;

public interface IZigguratHelper {

    TabEntry createFakePlayer(
            ZigguratTablist zigguratTablist, String string, TabColumn column, Integer slot, Integer rawSlot
    );

    void updateFakeName(
            ZigguratTablist zigguratTablist, TabEntry tabEntry, String text
    );

    void updateFakeLatency(
            ZigguratTablist zigguratTablist, TabEntry tabEntry, Integer latency
    );

    void updateFakeSkin(
            ZigguratTablist zigguratTablist, TabEntry tabEntry, SkinTexture skinTexture
    );

    void updateHeaderAndFooter(
            ZigguratTablist zigguratTablist, String header, String footer
    );
}
