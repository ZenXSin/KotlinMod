const lib = require("base/SFlib");
const TBLY = new Planet("测试星球", Planets.sun, 1, 3.3);
TBLY.meshLoader = prov(() => new MultiMesh(
	new HexMesh(TBLY, 8)
));
TBLY.cloudMeshLoader = prov(() => new MultiMesh(
	new HexSkyMesh(TBLY, 2, 0.15, 0.14, 5, Color.valueOf("25C9AB80"), 2, 0.42, 1, 0.43),
	new HexSkyMesh(TBLY, 3, 0.6, 0.15, 5, Color.valueOf("25C9ABFF"), 2, 0.42, 1.2, 0.45)
));
TBLY.generator = new SerpuloPlanetGenerator();
TBLY.visible = TBLY.accessible = TBLY.alwaysUnlocked =  true;
TBLY.clearSectorOnLose = false;
TBLY.tidalLock = false;
TBLY.localizedName = "测试星球";
TBLY.bloom = false;
TBLY.startSector = 1;
TBLY.orbitRadius = 85;
TBLY.orbitTime = 180 * 60;
TBLY.rotateTime = 90 * 60;
TBLY.atmosphereRadIn = 0.02;
TBLY.atmosphereRadOut = 0.3;
TBLY.atmosphereColor = TBLY.lightColor = Color.valueOf("25C9AB90");
TBLY.hiddenItems.addAll(Items.erekirItems).removeAll(Items.serpuloItems);

const 迫降区 = new SectorPreset("迫降区", TBLY, 1);
迫降区.description = "我们选择了一个敌人偏远的储存区进行突袭，夺下这个地区";
迫降区.difficulty = 2;
迫降区.alwaysUnlocked = false;
迫降区.addStartingItems = true;
迫降区.captureWave = 0;
迫降区.localizedName = "迫降区";
exports.迫降区 = 迫降区;
lib.addToResearch(迫降区, {
	parent: "planetaryTerminal",
	objectives: Seq.with(
	new Objectives.SectorComplete(SectorPresets.planetaryTerminal))
});