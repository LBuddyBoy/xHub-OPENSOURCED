package services.xenlan.hub.tablist.ziggurat.utils;

import lombok.Getter;
import lombok.Setter;
import services.xenlan.hub.tablist.ziggurat.ZigguratCommons;


@Getter
@Setter
public class BufferedTabObject {

    private TabColumn column = TabColumn.LEFT;
    private int ping = 0;
    private int slot = 1;
    private String text = "";
    private SkinTexture skinTexture = ZigguratCommons.defaultTexture;

    public BufferedTabObject text(String text){
        this.text = text;
        return this;
    }

    public BufferedTabObject skin(SkinTexture skinTexture){
        this.skinTexture = skinTexture;
        return this;
    }

    public BufferedTabObject slot(Integer slot){
        this.slot = slot;
        return this;
    }

    public BufferedTabObject ping(Integer ping){
        this.ping = ping;
        return this;
    }

    public BufferedTabObject column(TabColumn tabColumn){
        this.column = tabColumn;
        return this;
    }

}
