// RootLayout.tsx
"use client";

import { Inter } from "next/font/google";
import "./globals.css";
import Navbar from "@/Components/Navbar/Navbar";
import ClientOnly from "@/Components/ClientOnly/ClientOnly";
import { Suspense } from "react";
import Loading from "@/app/loading";
import AuthCheck from "@/Components/AuthCheck/AuthCheck";

const inter = Inter({ subsets: ["latin"] });

export default function RootLayout({
                                       children,
                                   }: {
    children: React.ReactNode;
}) {
    return (
        <html data-theme="winter" lang="en">
        <body className={inter.className}>
        <ClientOnly>
            <Suspense fallback={<Loading />}>
                <Navbar />
                <AuthCheck>
                    <Suspense fallback={<Loading />}>
                        {children}
                    </Suspense>
                </AuthCheck>
            </Suspense>
        </ClientOnly>
        </body>
        </html>
    );
}
