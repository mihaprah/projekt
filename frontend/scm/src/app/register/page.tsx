"use client"
import { createUserWithEmailAndPassword, sendEmailVerification  } from "firebase/auth";
import {auth} from "@/firebase";
import {useRouter} from "next/navigation";
import {useState} from "react";

const Register = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const router = useRouter();

    const register = async (event: React.FormEvent) => {
        event.preventDefault();
        try {
            const userCredential = await createUserWithEmailAndPassword(auth, email, password);
            const user = userCredential.user;
            if (user) {
                await sendEmailVerification(user);
                router.push('/');
            }
        } catch (error) {
            console.error(error);
        }
    };

    return (
        <div>
            <h1>Register</h1>
            <form onSubmit={register}>
                <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="Email" required />
                <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="Password" required />
                <button type="submit">Register</button>
            </form>
        </div>
    );
};

export default Register;