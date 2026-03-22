//
//  ArticlesViewModelWrapper.swift
//  iosApp
//
//  Created by Eli Pacheco Hoyos on 2/05/25.
//  Copyright © 2025 orgName. All rights reserved.
//
import SwiftUI
import shared
import KMPNativeCoroutinesAsync

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
        self.contentState = viewModel.contentState
        self.loaderState = viewModel.isRefreshing
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
        
        contentStateTask = Task { @MainActor in
            do {
                let sequence = asyncSequence(for: articlesViewModel.contentStateFlow)
                for try await state in sequence {
                    self.contentState = state
                }
            } catch {
                print("Articles contentState stream failed: \(error)")
            }
        }
        
        loaderTask = Task { @MainActor in
            do {
                let sequence = asyncSequence(for: articlesViewModel.isRefreshingFlow)
                for try await value in sequence {
                    self.loaderState = value.boolValue
                }
            } catch {
                print("Articles isRefreshing stream failed: \(error)")
            }
        }
    }
    
    func onRefreshContent() async {
        do {
            try await asyncFunction(for: articlesViewModel.onRefreshContentAsync())
        } catch {
            print("Could not refresh content: \(error)")
        }
    }
}
