//感谢神魂
importPackage(Packages.mindustry.graphics)

Events.on(EventType.ClientLoadEvent, () => {
const loadren = extend(MenuRenderer, {
    render(){
    Draw.color();
Draw.rect(Core.atlas.find("星辰-背景"), Core.graphics.getWidth() / 2, Core.graphics.getHeight() / 2, 1080, 2340);
    }
})

function Class(id) {
	return Seq([id]).get(0)
}

var fi = Class(MenuFragment).getDeclaredField("renderer");
fi.setAccessible(true);
fi.set(Vars.ui.menufrag, loadren);
})