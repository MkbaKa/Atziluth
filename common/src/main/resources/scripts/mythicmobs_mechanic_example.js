function onLoad() {
    registerMechanic(["test", "teeest"])
        .castAtEntity(function (skillMetadata, abstractEntity) {
            info("test mechanic running!")
            return false
        })
}