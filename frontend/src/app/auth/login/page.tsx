import AuthButton from "@/components/auth/AuthButton";

export default function LoginPage() {
    return (
        <div className="min-h-screen flex items-center justify-center">
            <div className="bg-white p-8 rounded-lg shadow-md">
                <h1 className="text-2xl font-bold mb-4">Sign In</h1>
                <AuthButton />
            </div>
        </div>
    );
}