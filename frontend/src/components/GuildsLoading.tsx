import { Skeleton } from "@/components/ui/skeleton";

export default function GuildsLoading() {
    return (
        <div>
            {new Array(200).fill(0).map((_, idx) => (
                <div key={idx} className={`flex items-center gap-4 px-4 py-2 my-1 ${idx === 0 ? "mt-2" : ""} ${idx === 199 ? "mb-2" : ""}`}>
                    <Skeleton className="flex-none w-10 h-10 rounded-full" />
                    <div className="flex-auto">
                        <Skeleton className="h-6" />
                    </div>
                </div>
            ))}
        </div>
    );
}
