/**
 *  ****************  Web Pinger  ****************
 *
 *  Design Usage:
 *  Monitor a website and trigger automations if not found or if your internet goes down.
 *
 *  Copyright 2018 Bryan Turcotte (@bptworld)
 *
 *  Special thanks to (@Cobra) for use of his Parent/Child code and various other bits and pieces.
 *  Also thanks to Jason Botello for the original 2016 'SmartPing' code that I based this app off of.
 *  
 *  This App is free.  If you like and use this app, please be sure to give a shout out on the Hubitat forums to let
 *  people know that it exists!  Thanks.
 *
 *  Remember...I am not a programmer, everything I do takes a lot of time and research!
 *  Donations are never necessary but always appreciated.  Donations to support development efforts are accepted via: 
 *
 *  Paypal at: https://paypal.me/bptworld
 *
 *-------------------------------------------------------------------------------------------------------------------
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 * ------------------------------------------------------------------------------------------------------------------------------
 *
 *  If modifying this project, please keep the above header intact and add your comments/credits below - Thank you! -  @BPTWorld
 *
 *  App and Driver updates can be found at https://github.com/bptworld/Hubitat/
 *
 * ------------------------------------------------------------------------------------------------------------------------------
 *
 *  Changes:
 *
 *  V1.0.1 - 12/10/19 - Tons of cosmetic changes. Added in Push option, time between polls. Changed it up to turn the switch off
 *						if website/internet becomes active again. Added in all the normal stuff - pause, enable/disable and debug
 *						logging.
 *  V1.0.0 - 12/09/19 - Hubitat Port of ST app 'SmartPing' - 2016 Jason Botello
 *
 */

definition(
	name: "Web Pinger",
	namespace: "BPTWorld",
	author: "Bryan Turcotte",
	description: "Monitor a website and trigger automations if not found or if your internet goes down.",
	category: "Convenience",
	iconUrl: "",
	iconX2Url: "",
	iconX3Url: "",
)

preferences {
     page name: "mainPage", title: "", install: true, uninstall: true
} 

def installed() {
    log.debug "Installed with settings: ${settings}"
    initialize()
}

def updated() {
    log.debug "Updated with settings: ${settings}"
    unsubscribe()
    initialize()
}

def initialize() {
    log.info "There are ${childApps.size()} child apps"
    childApps.each {child ->
    log.info "Child app: ${child.label}"
    }
}

def mainPage() {
    dynamicPage(name: "mainPage") {
    	installCheck()
		if(state.appInstalled == 'COMPLETE'){
			section(getFormat("title", "${app.label}")) {
				paragraph "<div style='color:#1A77C9'>Monitor a website and trigger automations if not found or if your internet goes down.</div>"
				paragraph getFormat("line")
			}
			section("Instructions:", hideable: true, hidden: true) {
				paragraph "<b>Notes:</b>"
				paragraph ""	
			}
  			section(getFormat("header-green", "${getImage("Blank")}"+" Child Apps")) {
				app(name: "anyOpenApp", appName: "Web Pinger Child", namespace: "BPTWorld", title: "<b>Add a new 'Web Pinger' child</b>", multiple: true)
			}
			section(getFormat("header-green", "${getImage("Blank")}"+" General")) {
       			label title: "Enter a name for parent app (optional)", required: false
 			}
			display()
		}
	}
}

def installCheck(){         
	state.appInstalled = app.getInstallationState() 
	if(state.appInstalled != 'COMPLETE'){
		section{paragraph "Please hit 'Done' to install '${app.label}' parent app "}
  	}
  	else{
    	log.info "Parent Installed OK"
  	}
}

def getImage(type) {
    def loc = "<img src=https://raw.githubusercontent.com/bptworld/Hubitat/master/resources/images/"
    if(type == "Blank") return "${loc}blank.png height=35 width=5}>"
}

def getFormat(type, myText=""){
	if(type == "header-green") return "<div style='color:#ffffff;font-weight: bold;background-color:#81BC00;border: 1px solid;box-shadow: 2px 3px #A9A9A9'>${myText}</div>"
    if(type == "line") return "\n<hr style='background-color:#1A77C9; height: 1px; border: 0;'></hr>"
	if(type == "title") return "<h2 style='color:#1A77C9;font-weight: bold'>${myText}</h2>"
}

def display(){
	section() {
		paragraph getFormat("line")
		paragraph "<div style='color:#1A77C9;text-align:center'>Web Pinger - App Version: 1.0.1 - @BPTWorld<br><a href='https://github.com/bptworld/Hubitat' target='_blank'>Find more apps on my Github, just click here!</a></div>"
	}       
}
