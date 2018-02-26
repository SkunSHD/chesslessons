(ns chesslessons.home.page
	(:require
;		History
		[chesslessons.history :as history :refer [nav!]]
;		Modles
		[chesslessons.visitor-model :as visitor_model]
;		Components
		[chesslessons.components.sign_in.components :as sign_in_components]
		[chesslessons.components.profile.components :as profile_components]
))


(defn render []
	[:div
	 (if (nil? @visitor_model/visitor)
		 [sign_in_components/render]
		 [profile_components/render])
	 ])