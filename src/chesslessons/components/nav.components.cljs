(ns chesslessons.components.nav.components
	 (:require
		 [chesslessons.firebase :as fbs]
		 [reagent.core :refer [atom cursor]]
;		 History
		 [chesslessons.history :as history :refer [nav!]]
;		 Models
		 [chesslessons.admin-model :as admin_model]
		 ))

(def log (.-log js/console))

(defn render_nav []
	[:nav.navbar.navbar-primary.bg-primary
	 [:span {:onClick #(nav! "/")}
	  [:img {:src "https://pbs.twimg.com/profile_images/471961293981626368/hGiM_c_R_400x400.png" :width 70 :height 70 }]]
	  (if-not (nil? @admin_model/admin) [:span.navbar-text "welcome, admin!"])
	 ])