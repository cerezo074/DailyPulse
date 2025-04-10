//
//  SettingsMenuView.swift
//  iosApp
//
//  Created by Eli Pacheco Hoyos on 8/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct SettingsMenuView: View {
    @ObservedObject
    var coordinator: SettingsCoordinator
    
    var body: some View {
        List {
            Button {
                coordinator.navigate(to: .profile)
            } label: {
                Text("Profile")
            }
            Button {
                coordinator.navigate(to: .about)
            } label: {
                Text("About")
            }
            Button {
                coordinator.openModal(screen: .shareFeedback)
            } label: {
                Text("Share feedback")
            }
        }
        .navigationTitle("Settings")
    }
}
