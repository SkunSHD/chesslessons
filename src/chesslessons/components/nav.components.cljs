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
	 (if (:profile @admin_model/admin)
		  [:span.navbar-text
		    [:img {:src (:url (:data (:picture (:profile @admin_model/admin)))) :width 50 :height 50}]]
		  [:span.navbar-text
		   [:img {:src "https://getbootstrap.com/assets/brand/bootstrap-solid.svg" :width 50 :height 50 }]]
		  )]
	 (if-not (nil? @admin_model/admin) [:span.navbar-text "welcome, admin!"])
	 ])