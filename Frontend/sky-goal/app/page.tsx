'use client'

import Image from "next/image";
import { useRouter } from 'next/navigation';

export default function Home() {
  const router = useRouter();

  return (
    <main className="flex justify-center items-center h-screen bg-dark-blue">
  <Image src={"/footballer.png"}
          alt="Soccer Player" 
          width={500}
          height={340}
          priority/>     
          <button onClick={()=>{router.push('/matchCardList');}} className="bg-white p-1.5 m-3 rounded-md"> Get Started</button>

    </main>
  );
}
