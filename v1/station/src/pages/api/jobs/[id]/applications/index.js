import http from "@/util/axios.util";

export default async function handler(req, res) {
  switch (req.method) {
    case "POST":
      submitApplication(req, res);
      break;
    case "GET":
      getApplications(req, res);
      break;
    default:
      break;
  }
}

async function submitApplication(req, res) {
  try {
    const response = await http(req, res).post(
      `/jobs/${req.query.id}/applications`
    );
    res.status(response.status).json(response.data);
  } catch (error) {
    res.status(400).json({});
  }
}

async function getApplications(req, res) {
  try {
    const response = await http(req, res).get(
      `/jobs/${req.query.id}/applications`
    );
    res.status(response.status).json(response.data);
  } catch (err) {
    res.status(400).json({});
  }
}
