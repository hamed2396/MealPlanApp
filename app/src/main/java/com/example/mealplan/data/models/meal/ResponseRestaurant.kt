import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class ResponseRestaurant(
    @SerializedName("restaurants")
    val restaurants: List<Restaurant>?
) : Parcelable

@Parcelize
data class Restaurant(
    @SerializedName("address")
    val address: Address?,
    @SerializedName("aggregated_rating_count")
    val aggregatedRatingCount: Int?,
    @SerializedName("cuisines")
    val cuisines: List<String?>?,
    @SerializedName("delivery_enabled")
    val deliveryEnabled: Boolean?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("dollar_signs")
    val dollarSigns: Int?,
    @SerializedName("food_photos")
    val foodPhotos: List<String?>?,
    @SerializedName("_id")
    val id: String?,
    @SerializedName("is_open")
    val isOpen: Boolean?,
    @SerializedName("local_hours")
    val localHours: LocalHours?,
    @SerializedName("logo_photos")
    val logoPhotos: List<String?>?,
    @SerializedName("miles")
    val miles: Double?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("offers_first_party_delivery")
    val offersFirstPartyDelivery: Boolean?,
    @SerializedName("offers_third_party_delivery")
    val offersThirdPartyDelivery: Boolean?,
    @SerializedName("phone_number")
    val phoneNumber: Long?,
    @SerializedName("pickup_enabled")
    val pickupEnabled: Boolean?,
    @SerializedName("store_photos")
    val storePhotos: @RawValue List<Any?>?,
    @SerializedName("supports_upc_codes")
    val supportsUpcCodes: Boolean?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("weighted_rating_value")
    val weightedRatingValue: Double?
) : Parcelable

@Parcelize
data class Address(
    @SerializedName("city")
    val city: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("lat")
    val lat: Double?,
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("lon")
    val lon: Double?,
    @SerializedName("longitude")
    val longitude: Double?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("street_addr")
    val streetAddr: String?,
    @SerializedName("street_addr_2")
    val streetAddr2: String?,
    @SerializedName("zipcode")
    val zipcode: String?
) : Parcelable

@Parcelize
data class LocalHours(
    @SerializedName("delivery")
    val delivery: Delivery?,
    @SerializedName("dine_in")
    val dineIn: DineIn?,
    @SerializedName("operational")
    val operational: Operational?,
    @SerializedName("pickup")
    val pickup: Pickup?
) : Parcelable

@Parcelize
data class Delivery(
    @SerializedName("Friday")
    val friday: String?,
    @SerializedName("Monday")
    val monday: String?,
    @SerializedName("Saturday")
    val saturday: String?,
    @SerializedName("Sunday")
    val sunday: String?,
    @SerializedName("Thursday")
    val thursday: String?,
    @SerializedName("Tuesday")
    val tuesday: String?,
    @SerializedName("Wednesday")
    val wednesday: String?
) : Parcelable

@Parcelize
data class DineIn(
    @SerializedName("Friday")
    val friday: String?,
    @SerializedName("Monday")
    val monday: String?,
    @SerializedName("Saturday")
    val saturday: String?,
    @SerializedName("Sunday")
    val sunday: String?,
    @SerializedName("Thursday")
    val thursday: String?,
    @SerializedName("Tuesday")
    val tuesday: String?,
    @SerializedName("Wednesday")
    val wednesday: String?
) : Parcelable

@Parcelize
data class Operational(
    @SerializedName("Friday")
    val friday: String?,
    @SerializedName("Monday")
    val monday: String?,
    @SerializedName("Saturday")
    val saturday: String?,
    @SerializedName("Sunday")
    val sunday: String?,
    @SerializedName("Thursday")
    val thursday: String?,
    @SerializedName("Tuesday")
    val tuesday: String?,
    @SerializedName("Wednesday")
    val wednesday: String?
) : Parcelable

@Parcelize
data class Pickup(
    @SerializedName("Friday")
    val friday: String?,
    @SerializedName("Monday")
    val monday: String?,
    @SerializedName("Saturday")
    val saturday: String?,
    @SerializedName("Sunday")
    val sunday: String?,
    @SerializedName("Thursday")
    val thursday: String?,
    @SerializedName("Tuesday")
    val tuesday: String?,
    @SerializedName("Wednesday")
    val wednesday: String?
) : Parcelable
