package sv.com.castroluna.pocketm.go.model

class Ability2 {
    var name: String? = null
    var url: String? = null
}

class Ability {
    var ability: Ability? = null
    var is_hidden = false
    var slot = 0
}

class Form {
    var name: String? = null
    var url: String? = null
}

class Version {
    var name: String? = null
    var url: String? = null
}

class GameIndice {
    var game_index = 0
    var version: Version? = null
}

class Move2 {
    var name: String? = null
    var url: String? = null
}

class MoveLearnMethod {
    var name: String? = null
    var url: String? = null
}

class VersionGroup1 {
    var name: String? = null
    var url: String? = null
}

class VersionGroupDetail {
    var level_learned_at = 0
    var move_learn_method: MoveLearnMethod? = null
    var version_group: VersionGroup? = null
}

class Move {
    var move: Move2? = null
    var version_group_details: List<VersionGroupDetail>? = null
}

class Species {
    var name: String? = null
    var url: String? = null
}

class DreamWorld {
    var front_default: String? = null
    var front_female: Any? = null
}

class OfficialArtwork {
    var front_default: String? = null
}



class RedBlue {
    var back_default: String? = null
    var back_gray: String? = null
    var front_default: String? = null
    var front_gray: String? = null
}

class Yellow {
    var back_default: String? = null
    var back_gray: String? = null
    var front_default: String? = null
    var front_gray: String? = null
}




class OmegarubyAlphasapphire {
    var front_default: String? = null
    var front_female: Any? = null
    var front_shiny: String? = null
    var front_shiny_female: Any? = null
}

class XY {
    var front_default: String? = null
    var front_female: Any? = null
    var front_shiny: String? = null
    var front_shiny_female: Any? = null
}


class Icons {
    var front_default: String? = null
    var front_female: Any? = null
}


class Stat2 {
    var name: String? = null
    var url: String? = null
}

class Stat {
    var base_stat = 0
    var effort = 0
    var stat: Stat? = null
}

class Type2a {
    var name: String? = null
    var url: String? = null
}

class Type3 {
    var slot = 0
    var type: Type2a? = null
}

class PokemonAbility {
    var abilities: List<Ability>? = null
    var base_experience = 0
    var forms: List<Form>? = null
    var game_indices: List<GameIndice>? = null
    var height = 0
    var held_items: List<Any>? = null
    var id = 0
    var is_default = false
    var location_area_encounters: String? = null
    var moves: List<Move>? = null
    var name: String? = null
    var order = 0
    var past_types: List<Any>? = null
    var species: Species? = null
    var sprites: Sprites? = null
    var stats: List<Stat>? = null
    var types: List<Type3>? = null
    var weight = 0
}
