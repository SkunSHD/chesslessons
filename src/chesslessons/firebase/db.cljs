(ns chesslessons.firebase.db
	(:require
		[reagent.core :refer [atom cursor]]
		[chesslessons.firebase :as fb]))

(def log (.-log js/console))


(def firestore (.firestore js/firebase))

(def collection {:users (.collection firestore "users")})


(defn write_user_data [formatted_user]
	(let [user_profile (:profile formatted_user) fbs_user (:fbs_user formatted_user)]
		(
			.add (:users collection)
				 (clj->js
				   {:email (:email user_profile)
					:link  (:link user_profile)
					:ava (:url (:data (:picture user_profile)))
					:location (:name (:location user_profile))
					:uid   (:uid fbs_user)})))
		)


(defn save_user [formatted_user]
	(write_user_data formatted_user)
	)