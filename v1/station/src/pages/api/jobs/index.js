import http from "@/util/axios.util";

export default async function handler(req, res) {
  switch (req.method) {
    case "GET":
      getPagedJobs(req, res);
      break;
    case "POST":
      postNewJob(req, res);
      break;
    default:
      break;
  }
}

async function getPagedJobs(req, res) {
  try {
    const response = await http(req, res).get("/jobs", { params: req.query });
    res.status(response.status).json(response.data);
  } catch (err) {
    res.status(400).json({});
  }
}

async function postNewJob(req, res) {
  try {
    const response = await http(req, res).post("/jobs", req.body);
    res.status(response.status).json(response.data);
  } catch (err) {
    console.log(err);
    res.status(400).json({});
  }
}
