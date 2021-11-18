(ns gpad.core
  (:require [reagent.core :as r]
            [reagent.react-native :as rn]
            [react-native :as rrn]
            [re-frame.core :as rf]
            ["@react-navigation/native" :refer [NavigationContainer]]
            ["@react-navigation/native-stack" :refer [createNativeStackNavigator]]

            [clojure.string :as str]
            [gpad.events :as events]
            [gpad.viewer :as viewer]
            [gpad.db :as db]
            [gpad.song-list :as song-list]))

(def nav-container (r/adapt-react-class NavigationContainer))

(defn log [d] (js/console.log d))

(def raw-lyric
  ["[Asus4]Here's a thought for every man who tries to understand"
   "what is in his [G]hands  (what's in his hands)"
   "He [Asus4]walks along the open road of love and life"
   "surviving if he [G]can   (only if he can)"
   "-"
   "[Em]Bound with all the [D]weight of all the [C]words he tried to [G5]say"
   "[Em]Chained to all the [D]places that he [C]never wished to [G5]stay"
   "[Em]Bound with all the [D]weight of all the [C]words he tried to [G5]say"
   "[Em]As he faced the [D]sun he [C]cast no shadow"
   ""
   "[Am][C][D][E]"
   "And [D]after all,[Asus7] "
   "[Asus4]Here's a thought for every man who tries to understand"
   "what is in his [G]hands  (what's in his hands)"
   "He [Asus4]walks along the open road of love and life"
   "surviving if he [G]can   (only if he can)"
   "-"
   "[Em]Bound with all the [D]weight of all the [C]words he tried to [G5]say"
   "[Em]Chained to all the [D]places that he [C]never wished to [G5]stay"
   "[Em]Bound with all the [D]weight of all the [C]words he tried to [G5]say"
   "[Em]As he faced the [D]sun he [C]cast no shadow"])

(defn prepare-navigator
  [navigator screen]
  (fn [& params]
    (let [[props children] (if (map? (first params))
                             [(first params) (second params)]
                             [{} (first params)])]
      (into [navigator props]
            (mapv (fn [props]
                    [screen (update props :component r/reactify-component)])
                  children)))))

(defn create-stack-navigator
  []
  (let [^js stack (createNativeStackNavigator)]
    [(r/adapt-react-class (.-Navigator stack))
     (r/adapt-react-class (.-Screen stack))]))

(defn stack
  [& params]
  (let [[navigator screen] (create-stack-navigator)]
    (prepare-navigator navigator screen)))

(comment
  [{:title "wanderwall"
    :artist "oasis"
    :lyric raw-lyric}])

; :on-click #(rf/dispatch [:navigate-to :song])
(def test-data
  [{:title "Song 2133"}
   {:title "Song 11"
    :lyric raw-lyric}])

(defn songs-page []
  (when-let [data (rf/subscribe [:data])]
    [song-list/song-list @data]))

(defn song-page []
  (if-let [raw-lyric @(rf/subscribe [:lyric])]
    [viewer/viewer raw-lyric]
    [rn/text "empty"])) ; todo add style for an empty state

(defn navigator-menu-button []
  (r/as-element
    [rn/button {:title "Go"
                :onPress #(rf/dispatch [:init-data test-data])}]))

(defn main-page []
  [rn/safe-area-view {:style {:flex 1}} #_{:style {:flex 1 :align-items "center" :justify-content "center"}}
   [nav-container {:ref #(reset! events/nav-ref-state %)}
    [stack
     [{:name      :main
       :component songs-page
       :options   {
                   :headerTitle
                               (fn []
                     (r/as-element [rn/text "test"]))
                   :headerRight navigator-menu-button
                   }
       }
      {:name      :song
       :component song-page}]]]])

(defn ^:export -main [& args]
  (r/as-element [main-page]))