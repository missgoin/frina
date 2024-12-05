//Constants.kt
package frena.id.data

// APP
const val MANAGER_APP_PACKAGE_NAME = "frena.id"
const val SHARED_PREFS_FILE = "xposed_shared_prefs"

// SERVICE NOTIFICATION FRINA LOCATION
const val name = "frina_service"
const val KEY_USE_FRINA_SERVICE = "use_frina_service"

// gojek
const val KEY_USE_GOJEK_BYPASS_REG = "use_gobypassreg"
const val KEY_GOJEK_BYPASS_REG = "gobypassreg"

const val KEY_USE_GOJEK_BYPASS_ACE = "use_gobypassace"
//const val KEY_GOBYPASSACE  = "gobypassace"

const val KEY_USE_GOJEK_AUTOKILL = "use_goautokill"
//const val KEY_GOAUTOKILL  = "goautokill"

const val KEY_USE_GOJEK_TS = "use_gots"
//const val KEY_GOTS  = "gots"


const val KEY_IS_PLAYING = "is_playing"

const val KEY_LAST_CLICKED_LOCATION = "last_clicked_location"

const val KEY_USE_ACCURACY = "use_accuracy"
const val KEY_ACCURACY  = "accuracy"

//const val KEY_USE_ALTITUDE = "use_altitude"
//const val KEY_ALTITUDE  = "altitude"

const val KEY_USE_RANDOMIZE  = "use_randomize"
const val KEY_RANDOMIZE_RADIUS = "randomize_radius"

const val KEY_USE_VERTICAL_ACCURACY = "use_vertical_accuracy"
const val KEY_VERTICAL_ACCURACY = "vertical_accuracy"




const val KEY_USE_SPEED = "use_speed"
const val KEY_SPEED = "speed"

const val KEY_USE_SPEED_ACCURACY = "use_speed_accuracy"
const val KEY_SPEED_ACCURACY = "speed_accuracy"


 // DEFAULT VALUES
 
const val DEFAULT_FRINA_SERVICE = "STOPPED"
 
 // gojek
const val DEFAULT_USE_GOJEK_BYPASS_REG = true
const val DEFAULT_GOJEK_BYPASS_REG = true

const val DEFAULT_USE_GOJEK_BYPASS_ACE = false

const val DEFAULT_USE_GOJEK_AUTOKILL = false
const val DEFAULT_USE_GOJEK_TS = false 
 
const val DEFAULT_USE_ACCURACY = false
const val DEFAULT_ACCURACY = 0.0

//const val DEFAULT_USE_ALTITUDE = false
//const val DEFAULT_ALTITUDE = 0.0

const val DEFAULT_USE_RANDOMIZE = true
const val DEFAULT_RANDOMIZE_RADIUS = 0.2

const val DEFAULT_USE_VERTICAL_ACCURACY = false
const val DEFAULT_VERTICAL_ACCURACY = 0.0f




const val DEFAULT_USE_SPEED = false
const val DEFAULT_SPEED = 0.0f

const val DEFAULT_USE_SPEED_ACCURACY = false
const val DEFAULT_SPEED_ACCURACY = 0.0f

// MATH & PHYS
const val PI = 3.14159265359
const val RADIUS_EARTH = 6378137.0 // Approximately Earth's radius in meters
