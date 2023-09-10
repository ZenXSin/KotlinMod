//信息栏
//进游戏显示 搬运请勿移除版权等声明信息
Events.on(EventType.ClientLoadEvent, cons(e => {
    var dialog = new BaseDialog("[red]星辰信息栏");
    dialog.cont.image(Core.atlas.find("星辰-图片")).row();;
    dialog.buttons.defaults().size(210, 64);
    dialog.buttons.button("@close", run(() => {
        dialog.hide();
    })).size(210, 64);
    dialog.cont.pane((() => {
        var table = new Table();   
         table.add("[red]下滑还有按键\n当前版本∶0.38正式版\n感谢鱼姐姐和ra和炽热和发生啥了事喵和晓伟的技术支持").left().growX().wrap().width(600).maxWidth(1000).pad(4).labelAlign(Align.left);
        table.row();
table.button("[blue]加QQ群", run(() => {
    var dialog2 = new BaseDialog("[blue]星辰模组[white]QQ[blue]群二维码");
    var table = new Table();
    dialog2.cont.image(Core.atlas.find("星辰-二维码")).row();;
    dialog2.buttons.defaults().size(210, 64);
    dialog2.buttons.button("@close", run(() => {
        dialog2.hide();
    })).size(210, 64);
       dialog2.show();
    })).size(210, 64).row();
table.button("[red]更新日志", run(() => {
    var dialog2 = new BaseDialog("[red]更新日志");
    var table = new Table();
	
	var t = new Table();
	t.add("[red]----更新日志----\n[blue]12月05日(0.36测试):[red]\n修复科技树\n正式改名为星辰\n新增炮台<I型舰载近防炮>\n修改小部分工厂贴图和消耗\n新增<粒子>为生产反物质\n<湮灭发电机>为使用反物质发电[blue]\n12月12日(0.37正式)∶[red]\n更新新炮台<耀辉>为导弹发射类炮台\n新开科技树以及一些贴图的修改[blue]\n12月14日(0.38测试)∶[red]\n更新了一部分炮台的发热特效\n新buff<湮灭>\n增加开机信息版[blue]\n12月16日(0.38测试)∶[red]\n修改'粒子'贴图\n名字和动画\n新名为<粒子对撞机>\n修改背景图片[blue]\n12月18日(0.38测试)[red]\n更新了新工厂'磁型压缩机'为多合成工厂\n[blue]12月19日(0.38正式版)[red]\n新增工厂<衰变布工厂>为生产布的工厂\n修改平衡[blue]\n12月24日（0.39测试版）[red]\n更新新工厂<反物质混流器>为制造液态反物质的工厂\n新液态<液态反物质>\n磁型压缩机新增配方并修改配方\n新配方为生产自塑合\n新物品<自塑合金>\n新增系列<音乐>\n新增炮台<脉冲>\n[blue]12月25日(0.39测试版)[red]\n新增效果<辐射>");
    dialog2.cont.add(new ScrollPane(t)).size(500, 600).row();
    dialog2.buttons.defaults().size(620, 64);
    dialog2.buttons.button("@close", run(() => {
        dialog2.hide();
    })).size(210, 64);
       dialog2.show();
    })).size(210, 64).row();//return table;
table.button("[red]mod剧情", run(() => {//0
    var dialog2 = new BaseDialog("[red]mod剧情");
    var table = new Table();
	
	var t = new Table();
	t.add("[red]----mod剧情----\n----决战----\n危机纪元4052年\n在与敌方第五军团交战中\n敌方节节败退\n我们随着敌方撤退的脚步来到了这里\n<溢裂要塞>\n我们在击退了敌方后\n发现了不属于这个文明的科技\n<余温>\n我们在余温的数据库里面发现了一条加密数据\n解密后是一串二进制代码\n'111001001011100010000000 111001001011100010101010 00110011 111001111011101010100111 111001101001011010000111 111001101001100010001110 111001011001000010010111 00101100 111001011001011110101111'\n或许我们可以试着解读一下\n敌方的第一军团要攻过来了\n我们可以试着使用这根据新技术改良的<星陨>\n");
    dialog2.cont.add(new ScrollPane(t)).size(500, 810).row();
    dialog2.buttons.defaults().size(210, 64);
    dialog2.buttons.button("@close", run(() => {
        dialog2.hide();
    })).size(210, 64);
       dialog2.show();
    })).size(210, 64);//1
        return table;
    })()).grow().center().maxWidth(620);
    dialog.show();
}));
//新科技作者提供