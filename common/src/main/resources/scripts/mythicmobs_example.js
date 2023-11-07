/**
 * 对于 MythicMobs的扩展示例
 * 该部分需要一定开发基础
 *
 * 注: 该功能修改后 需先重载 Atziluth 再重载 MythicMobs 才能生效
 */
function onLoad() {
    /**
     * 创建一个 MythicMobs 技能代理对象
     * 参数为技能的名字
     */
    createMythicMechanic("test", "teeest")
        /**
         * 设置目标为实体时触发的效果
         * 该方法需要传递一个 function (参数名你可以随便写，在代码中的类型是固定的)
         * 此处仅介绍参数的作用及包名 对象内的方法请自己去代码里看
         * 以示例代码为例 function 的参数:
         *      skillMetadata - 技能执行时的元数据
         *          4.x包名: io.lumine.xikage.mythicmobs.skills.SkillMetadata
         *          5.x包名: io.lumine.mythic.api.skills.SkillMetadata
         *      entity - 技能执行时的目标实体 若该技能选中了多个实体 则该技能会为每个实体执行一遍 类型为 LivingEntity
         *      args - 代理技能执行时的传参 类型为 Map<String, Any>
         *          "mythicLineConfig" - 技能执行时的参数 即 - test{xxx=true} 括号 {} 里的所有内容
         *          "caster" - 技能执行者 类型为 LivingEntity
         *          "target" - 技能目标 同上面的 entity 类型为 LivingEntity
         *          "entity" - 技能目标 同上面的 entity 类型为 LivingEntity
         *          "meta" - 技能元数据 同上面的 skillMetaData 类型为 SkillMetadata
         */
        .castAtEntity(function (skillMetadata, entity, args) {
            info(args["caster"].name + " 触发了 test 机制!")
            info("本次执行的技能目标为 " + entity.name + " !")
        })
        /**
         * 大部分与 castAtEntity 相同 只介绍不同之处
         * 参数 location - 技能执行时的目标坐标 类型为 Location
         */
        .castAtLocation(function (meta, location, args) {
            info(args["caster"].name + " 触发了 test 机制!")
            info("本次执行的技能目标为 " + location + " !")
        })
        /**
         * 注册技能
         */
        .register()

}