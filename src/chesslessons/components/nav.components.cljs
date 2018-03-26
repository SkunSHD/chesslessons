(ns chesslessons.components.nav.component
	 (:require
		 [chesslessons.firebase :as fbs]
		 [reagent.core :refer [atom cursor]]
;		 History
		 [chesslessons.history :as history :refer [nav! ?current_page]]
;		 Models
		 [chesslessons.admin-model :as admin_model]
         ))

(def log (.-log js/console))

(defn render []
	[:nav.navbar.navbar-primary.bg-primary
	 [:span {:onClick #(nav! "/") :style {:cursor "pointer"}}
	  (if-not (nil? @admin_model/admin)
		  [:img {:src (:photo @admin_model/admin) :height 70 }]
		  [:img {:src "https://cdn1.worldpng.ru/uploads/2018/3/25/4dc0d7601406a6114b1fe79754a3d2f5-full.png" :width 70 :height 70 }])
	  ]
	  (if (nil? @admin_model/admin)
          (if-not (?current_page "admin_page") [:button {:onClick #(nav! "/admin")} "Login as admin"])
          [:div.dropdown
			 [:button.btn.btn-secondary.dropdown-toggle {:type "button"
														 :id "dropdownMenuButton"
														 :data-toggle "dropdown"
														 :aria-haspopup "true"
														 :aria-expanded "false"} (:name @admin_model/admin)]
			 [:div.dropdown-menu {:aria-labelledby "dropdownMenuButton" :style {:right 0 :left "auto"}}
				[:span.dropdown-item {:onClick #(nav! "/admin/edit")} "Edit admin"]
				[:span.dropdown-item {:onClick #(nav! "/admin")} "Visitors"]
				[:span.dropdown-item {:onClick #(admin_model/log_out_admin)} "Log out"]
			  ]])
	 ])
