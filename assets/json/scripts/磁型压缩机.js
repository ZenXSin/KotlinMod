const library = require("base/library");
const 磁型压缩机 = library.MultiCrafter(GenericCrafter, GenericCrafter.GenericCrafterBuild, "磁型压缩机", [
  {
    input: {
      items: ["coal/10"],     
      liquids: ["water/32"],
      power: 3,
    },
    output: {
      items: ["graphite/40"],
    },
    craftTime: 15.5,
  }, 
  {
    input: {
      items: ["spore-pod/20"],     
      power: 12.8,
    },
    output: {
      liquids: ["oil/50"],
    },
    craftTime: 20,
  },
  {
    input: {
      items: ["titanium/10"],     
      liquids: ["oil/35"],
      power: 7,
    },
    output: {
      items: ["plastanium/10"],
    },
    craftTime: 15.5,
  },
  {
    input: {
      items: ["spore-pod/10"],     
      power: 18,
    },
    output: {
      liquids: ["water/40"],
    },
    craftTime: 8.5,
  },
  {
    input: {
      items: ["饱和火力-镄/20","饱和火力-铬/125","surge-alloy/20","饱和火力-莱普合金/25"],
      liquids: ["slag/12"],
      power: 25,
    },
    output: {
      items: ["星辰-自塑合金/10"],
    },
    craftTime: 55.5,
  }
]);
//copper=铜,lead=铅,metaglass=钢化玻璃,graphite=石墨,sand=沙子,coal=煤,titanium=钛,thorium=钍,scrap=废料,silicon=硅,plastanium=孢子,phase-fabric=相织位物,surge-alloy=合金,spore-pod=塑钢,blast-compound=爆炸混合物,pyratite=硫