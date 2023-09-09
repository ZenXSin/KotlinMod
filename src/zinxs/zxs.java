package zinxs;

import mindustry.mod.Mod;
public class zxs extends Mod{
    @Override
    public void loadContent(){
        Item.load();
        Block.load();
        TT.loadTechTree();
    }
}

