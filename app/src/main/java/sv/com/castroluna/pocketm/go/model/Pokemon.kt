package sv.com.castroluna.pocketm.go.model
//Class to get a pokemon listing
open class Pokemon {
    var name: String? = null
    var url: String? = null
}

class PokemonRoot {

    var count = 0
    var next: String? = null
    var previous: Any? = null
    var results: List<Pokemon>? = null

}


class PokemonPosition: Pokemon() {
    var latitude:Double=0.0
    var longitude:Double=0.0
}


class Sprites {
    var back_default: String? = null
    var back_female: Any? = null
    var back_shiny: String? = null
    var back_shiny_female: Any? = null
    var front_default: String? = null
    var front_female: Any? = null
    var front_shiny: String? = null
    var front_shiny_female: Any? = null
}

class Type2 {
    var name: String? = null
    var url: String? = null
}



class VersionGroup {
    var name: String? = null
    var url: String? = null
}

class PokemonDataRoot {
    var form_name: String? = null
    var form_names: List<Any>? = null
    var form_order = 0
    var id = 0
    var is_battle_only = false
    var is_default = false
    var is_mega = false
    var name: String? = null
    var names: List<Any>? = null
    var order = 0
    var pokemon: Pokemon? = null
    var sprites: Sprites? = null
    var types: List<Type2>? = null
    var version_group: VersionGroup? = null
}

