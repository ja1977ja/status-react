(ns status-im.ui.screens.wallet.collectibles.cryptostrikers
  (:require [re-frame.core :as re-frame]
            [status-im.i18n :as i18n]
            [status-im.ui.components.action-button.action-button :as action-button]
            [status-im.ui.components.colors :as colors]
            [status-im.ui.components.react :as react]
            [status-im.ui.screens.wallet.collectibles.styles :as styles]
            [status-im.ui.screens.wallet.collectibles.views :as collectibles]
            [status-im.utils.http :as http]))

(def strikers :STRK)

(defmethod collectibles/load-collectible-fx strikers [_ id]
  {:http-get {:url (str "https://us-central1-cryptostrikers-prod.cloudfunctions.net/cards/" id)
              :success-event-creator (fn [o]
                                       [:load-collectible-success strikers {id (http/parse-payload o)}])
              :failure-event-creator (fn [o]
                                       [:load-collectible-failure strikers {id (http/parse-payload o)}])}})

(defmethod collectibles/render-collectible strikers [_ {:keys [external_url description name image]}]
  [react/view {:style styles/details}
   [react/view {:style styles/details-text}
    [react/image {:style  styles/details-image
                  :source {:uri image}}]
    [react/view {:justify-content :center}
     [react/text {:style styles/details-name}
      name]
     [react/text
      description]]]
   [action-button/action-button {:label               (i18n/label :t/view-cryptostrikers)
                                 :icon                :icons/address
                                 :icon-opts           {:color colors/blue}
                                 :accessibility-label :open-collectible-button
                                 :on-press            #(re-frame/dispatch [:open-browser {:url external_url}])}]])
