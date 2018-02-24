(ns chesslessons.home.page
	(:require
;		History
		[chesslessons.history :as history :refer [nav!]]
;		Modles
		[chesslessons.visitor-model :as user_model]
;		Components
		[chesslessons.components.sign_in.components :as sign_in_components]
		[chesslessons.components.profile.components :as profile_components]
))


(defn render []
	[:div
	 (if (nil? @user_model/visitor)
		 [sign_in_components/render]
		 [profile_components/render])
	 ])