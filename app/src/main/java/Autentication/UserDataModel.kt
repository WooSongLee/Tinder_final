package Autentication

data class UserDataModel (
    val uid : String? = null,
    val nickname : String? = null,
    val gender : String? = null,
    val city : String? = null,
    val age : String? = null,
    val email :String? = null,
    val password : String? = null,
    val token : String? = null

    )
{
    // equals 오버라이드
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserDataModel) return false

        // 여기서 동등성을 어떻게 판단할지 정의
        return uid == other.uid &&
                nickname == other.nickname &&
                gender == other.gender &&
                city == other.city &&
                age == other.age &&
                email == other.email &&
                password == other.password
    }

    // hashCode 오버라이드
    override fun hashCode(): Int {
        var result = uid?.hashCode() ?: 0
        result = 31 * result + (nickname?.hashCode() ?: 0)
        result = 31 * result + (gender?.hashCode() ?: 0)
        result = 31 * result + (city?.hashCode() ?: 0)
        result = 31 * result + (age?.hashCode() ?: 0)
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (password?.hashCode() ?: 0)
        return result
    }
}