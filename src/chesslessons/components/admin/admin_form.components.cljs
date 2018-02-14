(ns chesslessons.components.admin.admin-form.components
	(:require
		[chesslessons.firebase.user.firebase :as fbs_user]
;		Models
		[chesslessons.admin-model :as admin_model]
))

(def log (.-log js/console))

; TODO: fix me
(defn render_admin_form []
	(log @admin_model/admin 42)
	[:div
	 [:form
	  [:h1.h3.mb-3.font-weight-normal "Edit admin info"]
	
	  [:div.form-group
	   [:label.sr-only "Name"]
	   [:input.form-control {
		    :placeholder "Name"
		    :defaultValue (:name @admin_model/admin)
		    :type "text"}]
	   ]
	
	  [:div.form-group
	   [:label.sr-only "Email"]
	   [:input.form-control {
		    :placeholder "Email"
		    :defaultValue (:email @admin_model/admin)
		    :type "email"}]
	   ]
	  ]
	 
	 [:div.form-group
	  [:button.btn.btn-primary { :on-click #(fbs_user/upodate_user (js-obj "displayName" "xxxx" "photoURL" "https://pbs.twimg.com/profile_images/471961293981626368/hGiM_c_R_400x400.png" ))} "Save"]]])