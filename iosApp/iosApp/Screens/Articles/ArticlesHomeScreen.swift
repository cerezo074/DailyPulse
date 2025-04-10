//
//  ArticlesHomeScreen.swift
//  iosApp
//
//  Created by Eli Pacheco Hoyos on 8/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct ArticlesHomeScreen: View {
    @ObservedObject
    var articlesCoordinator: ArticlesCoordinator
    
    var body: some View {
        NavigationStack(path: $articlesCoordinator.navigationStack) {
            articlesCoordinator.build(screen: .articles)
                .navigationDestination(for: ArticlesCoordinator.Screen.self) { articlesScreen in
                    articlesCoordinator.build(screen: articlesScreen)
                }
        }
    }
}
