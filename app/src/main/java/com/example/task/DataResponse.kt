package com.example.task

import com.google.gson.annotations.SerializedName

data class DataResponse(

	@field:SerializedName("response_code")
	val responseCode: String,

	@field:SerializedName("commonArr")
	val commonArr: CommonArr,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("content")
	val content: Content,

	@field:SerializedName("status")
	val status: String
)

data class RentalPhotos(

	@field:SerializedName("pre_inspection")
	val preInspection: PreInspection,

	@field:SerializedName("post_inspection")
	val postInspection: PostInspection
)

data class PreInspection(

	@field:SerializedName("front_left")
	val frontLeft: String,

	@field:SerializedName("back_right")
	val backRight: String,

	@field:SerializedName("front_right")
	val frontRight: String,

	@field:SerializedName("back_left")
	val backLeft: String
)

data class FareDetails(

	@field:SerializedName("total_cost")
	val totalCost: String,

	@field:SerializedName("total_time")
	val totalTime: String
)

data class Current(

	@field:SerializedName("reservation")
	val reservation: List<Any>,

	@field:SerializedName("rentals")
	val rentals: List<Any>
)

data class PostInspection(

	@field:SerializedName("front_left")
	val frontLeft: String,

	@field:SerializedName("back_right")
	val backRight: String,

	@field:SerializedName("front_right")
	val frontRight: String,

	@field:SerializedName("back_left")
	val backLeft: String
)

data class Vehicle(

	@field:SerializedName("car_icon")
	val carIcon: String,

	@field:SerializedName("vehicle_color")
	val vehicleColor: String,

	@field:SerializedName("license_plate")
	val licensePlate: String,

	@field:SerializedName("vin_number")
	val vinNumber: String,

	@field:SerializedName("info")
	val info: String
)

data class Response(

	@field:SerializedName("current")
	val current: Current,

	@field:SerializedName("history")
	val history: List<HistoryItem>
)

data class CommonArr(

	@field:SerializedName("token")
	val token: String
)

data class Content(

	@field:SerializedName("response")
	val response: Response
)

data class HistoryItem(

	@field:SerializedName("ended_station")
	val endedStation: String,

	@field:SerializedName("started_station")
	val startedStation: String,

	@field:SerializedName("started_paking_lane")
	val startedPakingLane: String,

	@field:SerializedName("rental_photos")
	val rentalPhotos: RentalPhotos,

	@field:SerializedName("started_at")
	val startedAt: String,

	@field:SerializedName("ended_parking_lane")
	val endedParkingLane: String,

	@field:SerializedName("fare_details")
	val fareDetails: FareDetails,

	@field:SerializedName("ended_at")
	val endedAt: String,

	@field:SerializedName("rental_id")
	val rentalId: String,

	@field:SerializedName("vehicle")
	val vehicle: Vehicle
)
