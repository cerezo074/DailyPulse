//
//  ArticlesViewModelWrapper.swift
//  iosApp
//
//  Created by Eli Pacheco Hoyos on 2/05/25.
//  Copyright © 2025 orgName. All rights reserved.
//
import SwiftUI
import shared

@MainActor
class ArticlesViewModelWrapper: ObservableObject {
    
    @Published
    private(set) var contentState: ArticlesState
    @Published
    private(set) var loaderState: Bool
    
    private var contentStateTask: Task<(), Never>?
    private var loaderTask: Task<(), Never>?
    let articlesViewModel: ArticlesViewModel
    
    init(articlesViewModel: ArticlesViewModel? = nil) {
        let viewModel = articlesViewModel ?? ArticlesInjector().viewModel
        self.contentState = viewModel.contentState.value
        self.loaderState = viewModel.isRefreshing.value.boolValue
        self.articlesViewModel = viewModel
    }
    
    deinit {
        articlesViewModel.clear()
        contentStateTask?.cancel()
        loaderTask?.cancel()
    }
    
    func startObservingChanges() async {
        contentStateTask?.cancel()
        loaderTask?.cancel()
        
        contentStateTask = Task {
            for await contentState in articlesViewModel.contentState {
                self.contentState = contentState
            }
        }
        
        loaderTask = Task {
            for await loaderState in articlesViewModel.isRefreshing {
                self.loaderState = loaderState.boolValue
            }
        }
    }
    
    func onRefreshContent() async {
        do {
            try await articlesViewModel.onRefreshContentAsync()
        } catch {
            print("Could not refresh content: \(error)")
        }
    }
}
