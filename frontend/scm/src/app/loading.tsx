export default function Loading() {
    return (
        <div className={""}>
            <div className={"flex flex-col justify-center items-center h-screen"}>
                <span className="loading loading-spinner loading-md"></span>
                <span className={"text-lg mt-4 font-semibold"}>Loading ...</span>
            </div>
        </div>
    )
}