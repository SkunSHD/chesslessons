(ns chesslessons.components.admin.admin-edit-form.component
	(:require
		[reagent.core :refer [atom]]
		[chesslessons.firebase.user :as fbs_user]
;		Models
		[chesslessons.admin-model :as admin_model]
))


(def log (.-log js/console))

; ==================
; Atoms
(defonce admin_info (atom {
	:displayName nil
	:photoURL nil
}))


; ==================
; Private
(defn- -set_admin_info [new_admin_info]
	(reset! admin_info new_admin_info))


(defn- -set_admin_info_field [key new_value]
	(swap! admin_info assoc key new_value))


(defn- -on_admin_form_submit []
	(log "on_admin_form_submit")
	(.then (fbs_user/upodate_user
	    (clj->js {
			:displayName (or(:displayName @admin_info) (:name @admin_model/admin))
			:photoURL (or (:photoURL @admin_info) (:photo @admin_model/admin))
		})) #(admin_model/set_admin fbs_user/user)))



(defn render []
	(log @admin_model/admin)
	[:div
	 [:form {:style {:text-align "left"}}
	  [:h1.h3.mb-3.font-weight-normal "Edit admin info"]
	
	  [:div.form-group
	   [:label "Name"]
	   [:input.form-control {
		    :placeholder "Name"
		    :onChange (fn [e] (-set_admin_info_field :displayName (.-value (.-target e))))
		    :value (or(:displayName @admin_info) (:name @admin_model/admin))
		    :type "text"}]
	   ]
	
	  [:div.form-group
	   [:label "Photo"]
	   [:img {:src (:photoURL @admin_info) :height 70 :style {:padding "5px 10px 5px 10px"}}]
	   [:input.form-control {
	        :placeholder "Photo"
	        :onChange (fn [e] (-set_admin_info_field :photoURL (.-value (.-target e))))
	        :value (or (:photoURL @admin_info) (:photo @admin_model/admin))
	        :type "text"}]
	   ]
	  ]
	 
	 [:div.form-group
	  [:button.btn.btn-primary { :on-click -on_admin_form_submit} "Save"]]])