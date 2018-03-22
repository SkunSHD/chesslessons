(ns chesslessons.home.page
	(:require
;		History
		[chesslessons.history :as history :refer [nav!]]
;		Modles
		[chesslessons.visitor-model :as visitor_model]
;		Components
		[chesslessons.components.sign_in.component :as sign_in_component]
		[chesslessons.components.profile.component :as profile_component]
))


(defn render []
	[:div.container
	 [sign_in_component/render]
	 (if-not (nil? @visitor_model/visitor)
		 [profile_component/render])
	 ])
