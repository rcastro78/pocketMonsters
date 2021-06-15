package sv.com.castroluna.pocketm.go.model

import java.util.*


open class PokemonCaptured {
    var id = 0
    var name: String? = null
    var captured_at: Date? = null
}

class Friend {
    var name: String? = null
    var pokemon: PokemonCaptured? = null
    var pkImage:String?=null
    var captured:String?=null

}

class Foe {
    var name: String? = null
    var pokemon: PokemonCaptured? = null
    var pkImage:String?=null
    var captured:String?=null
}

class Community {
    var friends: List<Friend>? = null
    var foes: List<Foe>? = null
}


