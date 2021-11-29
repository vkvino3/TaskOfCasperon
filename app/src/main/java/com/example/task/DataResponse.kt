package com.example.task

import com.google.gson.annotations.SerializedName

data class DataResponse(

	@field:SerializedName("response_code")
	val responseCode: String? = null,

	@field:SerializedName("commonArr")
	val commonArr: CommonArr? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("content")
	val content: Content? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class RentalPhotos(

	@field:SerializedName("pre_inspection")
	val preInspection: PreInspection? = null,

	@field:SerializedName("post_inspection")
	val postInspection: PostInspection? = null
)

data class PreInspection(

	@field:SerializedName("front_left")
	val frontLeft: String? = null,

	@field:SerializedName("back_right")
	val backRight: String? = null,

	@field:SerializedName("front_right")
	val frontRight: String? = null,

	@field:SerializedName("back_left")
	val backLeft: String? = null
)

data class HistoryItem(

	@field:SerializedName("ended_station")
	val endedStation: String? = null,

	@field:SerializedName("started_station")
	val startedStation: String? = null,

	@field:SerializedName("started_paking_lane")
	val startedPakingLane: String? = null,

	@field:SerializedName("rental_photos")
	val rentalPhotos: RentalPhotos? = null,

	@field:SerializedName("started_at")
	val startedAt: String? = null,

	@field:SerializedName("ended_parking_lane")
	val endedParkingLane: String? = null,

	@field:SerializedName("fare_details")
	val fareDetails: FareDetails? = null,

	@field:SerializedName("ended_at")
	val endedAt: String? = null,

	@field:SerializedName("rental_id")
	val rentalId: String? = null,

	@field:SerializedName("vehicle")
	val vehicle: Vehicle? = null
)

data class FareDetails(

	@field:SerializedName("total_cost")
	val totalCost: String? = null,

	@field:SerializedName("total_time")
	val totalTime: String? = null
)

data class Current(

	@field:SerializedName("reservation")
	val reservation: List<Any?>? = null,

	@field:SerializedName("rentals")
	val rentals: List<Any?>? = null
)

data class Vehicle(

	@field:SerializedName("car_icon")
	val carIcon: String? = null,

	@field:SerializedName("vehicle_color")
	val vehicleColor: String? = null,

	@field:SerializedName("license_plate")
	val licensePlate: String? = null,

	@field:SerializedName("vin_number")
	val vinNumber: String? = null,

	@field:SerializedName("info")
	val info: String? = null
)

data class PostInspection(

	@field:SerializedName("front_left")
	val frontLeft: String? = null,

	@field:SerializedName("back_right")
	val backRight: String? = null,

	@field:SerializedName("front_right")
	val frontRight: String? = null,

	@field:SerializedName("back_left")
	val backLeft: String? = null
)

data class Response(

	@field:SerializedName("current")
	val current: Current? = null,

	@field:SerializedName("history")
	val history: List<HistoryItem?>? = null
)

data class CommonArr(

	@field:SerializedName("token")
	val token: String? = null
)

data class Content(

	@field:SerializedName("response")
	val response: Response? = null
)
