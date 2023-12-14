const lib = require("lib");
//const khcore = require("techTree");
//const 0721构造器 = extend(UnitFactory, "0721构造器", {});
const kurh = new JavaAdapter(Planet, {
    load() {
        this.meshLoader = prov(() => new HexMesh(kurh, 6));
        this.super$load();
    }
}, "kurh", Planets.sun, 1);
exports.kurh = kurh;
const sS = require("sectorSize");
sS.planetGrid(kurh, 3);
kurh.generator = new SerpuloPlanetGenerator();
kurh.atmosphereColor = Color.valueOf("#d3661e");
kurh.atmosphereRadIn = 0.06;
kurh.atmosphereRadOut = 0.6;
kurh.localizedName = "[orange]Kurh";
//kurh.defaultCore = "khcore"
kurh.visible = true;
kurh.bloom = false;
kurh.accessible = true;
kurh.alwaysUnlocked = true;
kurh.startSector = 15;
kurh.orbitRadius = 20;