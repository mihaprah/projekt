// RootLayout.tsx
"use client";

import { Inter } from "next/font/google";
import "./globals.css";
import Navbar from "@/Components/Navbar/Navbar";
import ClientOnly from "@/Components/ClientOnly/ClientOnly";
import { Suspense } from "react";
import Loading from "@/app/loading";
import AuthCheck from "@/Components/AuthCheck/AuthCheck";
import { ToastContainer} from "react-toastify";

const inter = Inter({ subsets: ["latin"] });

export default function RootLayout({
                                       children,
                                   }: {
    children: React.ReactNode;
}) {
    return (
        <html data-theme="winter" lang="en">
        <head>
            <title>SCM</title>
        </head>
        <body className={inter.className}>
        <ClientOnly>
            <Suspense fallback={<Loading/>}>
                <Navbar/>
                <ToastContainer/>
                <AuthCheck>
                    <Suspense fallback={<Loading/>}>
                        {children}
                    </Suspense>Ï
                </AuthCheck>
            </Suspense>
        </ClientOnly>
        </body>
        </html>
    );
}
