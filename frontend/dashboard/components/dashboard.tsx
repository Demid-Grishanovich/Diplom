"use client"

import { useState } from "react"
import { Plus, CheckCircle2, Clock } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { ProjectCard } from "@/components/project-card"
import { CreateProjectModal } from "@/components/create-project-modal"

export interface Project {
  id: string
  title: string
  description: string
  progress: number
  status: "active" | "completed"
  contributors: number
  tasksCompleted: number
}

const initialProjects: Project[] = [
  {
    id: "1",
    title: "Medical Image Classification",
    description: "Classify medical X-ray images for disease detection",
    progress: 45,
    status: "active",
    contributors: 128,
    tasksCompleted: 4520,
  },
  {
    id: "2",
    title: "Sentiment Analysis - Social Media",
    description: "Analyze sentiment in social media posts across multiple platforms",
    progress: 78,
    status: "active",
    contributors: 256,
    tasksCompleted: 12400,
  },
  {
    id: "3",
    title: "Urban Scene Understanding",
    description: "Label objects and elements in urban street scenes",
    progress: 100,
    status: "completed",
    contributors: 89,
    tasksCompleted: 8920,
  },
  {
    id: "4",
    title: "Audio Transcription - Medical",
    description: "Transcribe and validate medical consultation recordings",
    progress: 62,
    status: "active",
    contributors: 142,
    tasksCompleted: 7240,
  },
  {
    id: "5",
    title: "Product Review Categorization",
    description: "Categorize customer reviews by topic and sentiment",
    progress: 91,
    status: "active",
    contributors: 203,
    tasksCompleted: 15680,
  },
  {
    id: "6",
    title: "Biodiversity Species Identification",
    description: "Identify and classify species in nature photographs",
    progress: 100,
    status: "completed",
    contributors: 167,
    tasksCompleted: 11200,
  },
]

export function Dashboard() {
  const [projects, setProjects] = useState<Project[]>(initialProjects)
  const [isModalOpen, setIsModalOpen] = useState(false)

  const handleCreateProject = (
    newProject: Omit<Project, "id" | "progress" | "contributors" | "tasksCompleted" | "status">,
  ) => {
    const project: Project = {
      ...newProject,
      id: `${Date.now()}`,
      progress: 0,
      contributors: 0,
      tasksCompleted: 0,
      status: "active",
    }
    setProjects([project, ...projects])
    setIsModalOpen(false)
  }

  const activeProjects = projects.filter((p) => p.status === "active")
  const completedProjects = projects.filter((p) => p.status === "completed")

  return (
    <div className="min-h-screen bg-background">
      {/* Header */}
      <header className="border-b border-border bg-card sticky top-0 z-10">
        <div className="max-w-7xl mx-auto px-6 py-6 flex items-center justify-between">
          <div>
            <h1 className="text-3xl font-bold text-foreground">DataCrowd Lab</h1>
            <p className="text-muted-foreground text-sm mt-1">Manage and monitor your crowdsourced data projects</p>
          </div>
          <Button
            onClick={() => setIsModalOpen(true)}
            className="bg-primary hover:bg-primary/90 text-primary-foreground gap-2"
          >
            <Plus className="w-4 h-4" />
            Create New Project
          </Button>
        </div>
      </header>

      <main className="max-w-7xl mx-auto px-6 py-8">
        {/* Active Projects Section */}
        <section className="mb-12">
          <div className="flex items-center gap-2 mb-6">
            <Clock className="w-5 h-5 text-primary" />
            <h2 className="text-2xl font-semibold text-foreground">Active Projects</h2>
            <span className="ml-auto text-sm text-muted-foreground bg-muted px-3 py-1 rounded-full">
              {activeProjects.length} projects
            </span>
          </div>

          {activeProjects.length > 0 ? (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {activeProjects.map((project) => (
                <ProjectCard key={project.id} project={project} />
              ))}
            </div>
          ) : (
            <Card className="bg-muted/30">
              <CardContent className="pt-8 pb-8 text-center">
                <p className="text-muted-foreground">No active projects yet. Create one to get started!</p>
              </CardContent>
            </Card>
          )}
        </section>

        {/* Completed Projects Section */}
        {completedProjects.length > 0 && (
          <section>
            <div className="flex items-center gap-2 mb-6">
              <CheckCircle2 className="w-5 h-5 text-green-600" />
              <h2 className="text-2xl font-semibold text-foreground">Completed Projects</h2>
              <span className="ml-auto text-sm text-muted-foreground bg-muted px-3 py-1 rounded-full">
                {completedProjects.length} projects
              </span>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {completedProjects.map((project) => (
                <ProjectCard key={project.id} project={project} />
              ))}
            </div>
          </section>
        )}
      </main>

      {/* Create Project Modal */}
      <CreateProjectModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onCreateProject={handleCreateProject}
      />
    </div>
  )
}
