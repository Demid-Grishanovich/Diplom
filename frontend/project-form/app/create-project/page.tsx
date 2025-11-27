import { CreateProjectForm } from "@/components/create-project-form"

export const metadata = {
  title: "Create New Project",
  description: "Create and configure a new data labeling project",
}

export default function CreateProjectPage() {
  return (
    <main className="min-h-screen bg-background py-12 px-4 sm:px-6 lg:px-8">
      <div className="mx-auto max-w-2xl">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-foreground">Create New Project</h1>
          <p className="mt-2 text-muted-foreground">
            Set up a new data labeling project and configure quality control settings
          </p>
        </div>
        <CreateProjectForm />
      </div>
    </main>
  )
}
