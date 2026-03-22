//
//  ArticlesScreen.swift
//  iosApp
//
//  Created by Eli Pacheco Hoyos on 7/04/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import shared
import KMPObservableViewModelSwiftUI
import KMPNativeCoroutinesAsync

struct ArticlesScreen: View {
    
    @StateViewModel var viewModel = ArticlesInjector().viewModel
    
    var body: some View {
        VStack {
            if viewModel.contentState.loading {
                ProgressView("Loading...")
            } else if !viewModel.contentState.articles.isEmpty {
                makeArticleList(with: viewModel.contentState.articles)
            } else if let errorMessage = viewModel.contentState.error {
                Text(errorMessage)
            }
        }
        .padding(.top, 40)
        .padding(.horizontal, 20)
        .navigationTitle("Articles")
        .navigationBarTitleDisplayMode(.inline)
    }
    
    private func makeArticleList(with articles: [Article]) -> some View {
        ScrollView(.vertical) {
            LazyVStack {
                ForEach(articles, id: \.self) { article in
                    ArticleRowView(with: article)
                }
            }
        }.refreshable {
            do {
                try await asyncFunction(for: viewModel.onRefreshContentAsync())
            } catch {
                print("Articles pull-to-refresh failed: \(error)")
            }
        }
    }
}

#Preview {
    ArticlesScreen()
}

extension Article: @retroactive Identifiable {
    public var id: String {
        "\(title) \(imageURL)"
    }
}
