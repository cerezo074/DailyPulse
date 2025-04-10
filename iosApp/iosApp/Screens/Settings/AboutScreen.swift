//
//  AboutScreen.swift
//  iosApp
//
//  Created by Eli Pacheco Hoyos on 14/03/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import shared

struct AboutScreen: View {
    private let viewModel: DeviceAttributeViewModel = .init()

    var body: some View {
        VStack(alignment: .leading) {
            ScrollView {
                LazyVStack(alignment: .leading) {
                    ForEach(viewModel.attributes) { attribute in
                        VStack(alignment: .leading, spacing: 2) {
                            Text(attribute.key)
                                .font(.caption2)
                                .foregroundStyle(Color.gray)
                            Text(attribute.value)
                                .font(.callout)
                                .foregroundStyle(Color.black)
                        }.padding(.bottom, 4)
                    }
                }
            }
            .padding(.top, 30)
            .padding(.horizontal, 12)
        }.navigationTitle("About Device")
    }
}


class DeviceAttributeViewModel {
    let attributes: [DeviceAttribute]
    private let platform: Platform
    
    init() {
        self.platform = Platform()
        platform.lofSystemInfo()
        attributes = [
            .init(key: "Operating System", value: "\(platform.osName) \(platform.osVersion)"),
            .init(key: "Device", value: "\(platform.deviceModel)"),
            .init(key: "Density", value: "\(platform.density)")
        ]
    }
}

struct DeviceAttribute: Identifiable {
    var id: String { key }
    var key: String
    var value: String
}

#Preview {
    AboutScreen()
}
